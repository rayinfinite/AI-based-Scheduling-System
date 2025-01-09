package com.github.rayinfinite.scheduler.ga_course;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.ga_course.config.Individual;
import com.github.rayinfinite.scheduler.utils.PublicHoliday;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Getter
public class Timetable {
    @Getter
    private final Map<Integer, Classroom> rooms;
    private final Map<Integer, Course> courses;
    private final Map<Integer, Cohort> cohorts;
    private final Map<Integer, Timeslot> timeslots;
    private final Map<Integer, Professor> professors;

    @Getter
    private TeachingPlan[] plans;

    private int plansNum = 0;

    //浅拷贝，用于适应度计算
    public Timetable(Timetable cloneable) {
        this.rooms = cloneable.getRooms();
        this.courses = cloneable.getCourses();
        this.cohorts = cloneable.getCohorts();
        this.timeslots = cloneable.getTimeslots();
        this.professors = cloneable.getProfessors();
    }

    public Timetable(Map<Integer, Course> inputDataMap,
                     Map<Integer, Cohort> cohortMap,
                     Map<Integer, Timeslot> timeslotMap,
                     Map<Integer, Classroom> classroomMap,
                     Map<Integer, Professor> professorMap) {
        this.rooms = classroomMap;
        this.courses = inputDataMap;
        this.cohorts = cohortMap;
        this.timeslots = timeslotMap;
        this.professors = professorMap;
        this.plansNum = 0;
    }

//    public void createPlans(Individual individual) {
//        int totalPlans = 0;
//
//        // Calculate total plans based on InputData
//        for (Course course : this.courses.values()) {
//            totalPlans += course.getDuration();
//        }
////        for (InputData course : this.courses.values()) {
////            totalPlans += course.getRun();
////        }
//        TeachingPlan[] plans = new TeachingPlan[totalPlans];
//
//        int[] chromosome = individual.getChromosome();
//        int chromosomePos = 0;
//        int planIndex = 0;
//
//        // Iterate through cohorts and their corresponding courses
//        for (Cohort cohort : this.getCohortsAsArray()) {
//            String CohortType = cohort.getCohortType();
//            for (Course course : this.getCoursesByCohortId(cohort.getId())) {
//                int duration = course.getDuration();
//
//                for (int dayOffset = 0; dayOffset < duration; dayOffset++) {
//                    plans[planIndex] = new TeachingPlan(planIndex, cohort.getId(), course.getId());
//
//                    int timeslotId = chromosome[chromosomePos] + dayOffset;
//                    if (timeslotId > this.getMaxTimeslotId()) {
//                        timeslotId = chromosome[chromosomePos];
//                    }
//
//                    Timeslot timeslot = this.getTimeslotById(timeslotId);
//
//                    plans[planIndex].addTimeslot(timeslotId);
//
//                    if (dayOffset == 0) {
//                        plans[planIndex].setRoomId(chromosome[chromosomePos + 1]);
//                    } else {
//                        plans[planIndex].setRoomId(plans[planIndex - 1].getRoomId());
//                    }
//
//                    if (dayOffset == 0) {
//                        int[] teacherIds = course.getTeacherIds();
//                        plans[planIndex].addProfessor1(teacherIds.length > 0 ? teacherIds[0] : 0);
//                        plans[planIndex].addProfessor2(teacherIds.length > 1 ? teacherIds[1] : 0);
//                        plans[planIndex].addProfessor3(teacherIds.length > 2 ? teacherIds[2] : 0);
//                    } else {
//                        plans[planIndex].addProfessor1(plans[planIndex - 1].getProfessor1Id());
//                        plans[planIndex].addProfessor2(plans[planIndex - 1].getProfessor2Id());
//                        plans[planIndex].addProfessor3(plans[planIndex - 1].getProfessor3Id());
//                    }
//
//                    planIndex++;
//                }
//                chromosomePos += 5;
//            }
//        }
//        this.plans = plans;
//    }



    public void createPlans(Individual individual) {
        int totalPlans = 0;

        // Calculate total plans based on InputData
        for (Course course : this.courses.values()) {
            totalPlans += course.getDuration();
        }

        TeachingPlan[] plans = new TeachingPlan[totalPlans];
        int[] chromosome = individual.getChromosome();
        int chromosomePos = 0;
        int planIndex = 0;

        // Iterate through cohorts and their corresponding courses
        for (Cohort cohort : this.getCohortsAsArray()) {
            String cohortType = cohort.getCohortType();

            for (Course course : this.getCoursesByCohortId(cohort.getId())) {
                int duration = course.getDuration();

                for (int dayOffset = 0; dayOffset < duration; dayOffset++) {
                    plans[planIndex] = new TeachingPlan(planIndex, cohort.getId(), course.getId());

                    // 根据 cohortType 选择合适的时间段
                    int timeslotId = selectTimeslot(chromosome, chromosomePos, dayOffset, cohortType);

                    plans[planIndex].addTimeslot(timeslotId);

                    if (dayOffset == 0) {
                        plans[planIndex].setRoomId(chromosome[chromosomePos + 1]);
                    } else {
                        plans[planIndex].setRoomId(plans[planIndex - 1].getRoomId());
                    }

                    if (dayOffset == 0) {
                        int[] teacherIds = course.getTeacherIds();
                        plans[planIndex].addProfessor1(teacherIds.length > 0 ? teacherIds[0] : 0);
                        plans[planIndex].addProfessor2(teacherIds.length > 1 ? teacherIds[1] : 0);
                        plans[planIndex].addProfessor3(teacherIds.length > 2 ? teacherIds[2] : 0);
                    } else {
                        plans[planIndex].addProfessor1(plans[planIndex - 1].getProfessor1Id());
                        plans[planIndex].addProfessor2(plans[planIndex - 1].getProfessor2Id());
                        plans[planIndex].addProfessor3(plans[planIndex - 1].getProfessor3Id());
                    }

                    planIndex++;
                }
                chromosomePos += 5;
            }
        }
        this.plans = plans;
    }

    // 辅助方法：根据 cohortType 选择合适的时间段
    private int selectTimeslot(int[] chromosome, int chromosomePos, int dayOffset, String cohortType) {
        // 获取初始的 timeslotId
        int initialTimeslotId = chromosome[chromosomePos] + dayOffset;

        // 如果 cohortType 为 "0"，直接返回初始的 timeslotId
        if ("0".equals(cohortType)) {
            return Math.min(initialTimeslotId, this.getMaxTimeslotId());
        }

        // 如果 cohortType 为 "1"，确保选择的时间段在周五或周六
        if ("1".equals(cohortType)) {
            List<Integer> availableTimeslots = getAvailableFridaySaturdayTimeslots();
            if (!availableTimeslots.isEmpty()) {
                // 从符合条件的时间段中随机选择一个
                Random random = new Random();
                return availableTimeslots.get(random.nextInt(availableTimeslots.size()));
            }
        }

        // 默认情况下，返回初始的 timeslotId
        return Math.min(initialTimeslotId, this.getMaxTimeslotId());
    }

    // 辅助方法：获取所有周五和周六的时间段
    private List<Integer> getAvailableFridaySaturdayTimeslots() {
        List<Integer> availableTimeslots = new ArrayList<>();
        for (int i = 0; i <= this.getMaxTimeslotId(); i++) {
            Timeslot timeslot = this.getTimeslotById(i);
            if (timeslot != null && isFridayOrSaturday(timeslot)) {
                availableTimeslots.add(i);
            }
        }
        return availableTimeslots;
    }

    // 辅助方法：判断时间段是否为周五或周六
    private boolean isFridayOrSaturday(Timeslot timeslot) {
        if (timeslot == null || timeslot.getDate() == null) {
            return false;
        }

        // 将 Date 对象转换为 LocalDateTime 对象
        LocalDateTime localDateTime = timeslot.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 获取星期几
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();

        // 判断是否为周五或周六
        return dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY;
    }


    private Timeslot getTimeslotById(int timeslotId) {
        if (timeslotId < 0 || timeslotId > this.getMaxTimeslotId()) {
            return null; // 或者抛出异常，具体取决于您的业务需求
        }
        return this.timeslots.get(timeslotId);
    }

    public List<Course> getCoursesByCohortId(int cohortId) {
        List<Course> cohortCourses = new ArrayList<>();
        for (Course course : this.courses.values()) {
            if (course.getCohortId() == cohortId) {
                cohortCourses.add(course);
            }
        }
        return cohortCourses;
    }

    public int getMaxTimeslotId() {
        Set<Integer> timeslotIds = this.timeslots.keySet();
        if (timeslotIds.isEmpty()) {
            return -1;
        }
        return Collections.max(timeslotIds);
    }

    public Classroom getRoom(int roomId) {
        if (!this.rooms.containsKey(roomId)) {
            log.info("Rooms doesn't contain key {}", roomId);
        }
        return this.rooms.get(roomId);
    }

    public Classroom getRandomRoom() {
        Object[] roomsArray = this.rooms.values().toArray();
        return (Classroom) roomsArray[(int) (roomsArray.length * Math.random())];
    }

    public Course getCourse(int courseId) {
        return this.courses.get(courseId);
    }

    public Cohort getCohort(int cohortId) {
        return this.cohorts.get(cohortId);
    }

    public Cohort[] getCohortsAsArray() {
        return this.cohorts.values().toArray(new Cohort[0]);
    }

    public Timeslot getTimeslot(int timeslotId) {
        return this.timeslots.get(timeslotId);
    }

    public Timeslot getRandomTimeslot() {
        Object[] timeslotArray = this.timeslots.values().toArray();
        return (Timeslot) timeslotArray[(int) (timeslotArray.length * Math.random())];
    }

    public int getPlansNum(Timetable timetable) {
        return timetable.getCourses().size();
    }

    public int calcClashes() {
        int clashes = 0;

        // 根据 roomId 和 timeslotId 分组
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        // 容量检查：遍历一次所有计划
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                clashes++;
            }
        }

        // 检查房间与时间段的冲突
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        clashes++;
                    }
                }
            }
        }

        // 检查教授的冲突
        Map<Integer, List<TeachingPlan>> professorTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int professorNum = this.getCourse(plan.getCourseId()).getProfessorNum();
            int timeslotId = plan.getTimeslotId();

            if (professorNum >= 1) {
                int key1 = plan.getProfessor1Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key1, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum >= 2) {
                int key2 = plan.getProfessor2Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key2, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum == 3) {
                int key3 = plan.getProfessor3Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key3, k -> new ArrayList<>()).add(plan);
            }
        }

        // 检查教授时间段冲突
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                clashes += group.size() - 1;
            }
        }

        // 根据 cohortType 限制排课时间段
        for (TeachingPlan plan : this.plans) {
            int cohortId = plan.getCohortId();
            Cohort cohort = this.getCohort(cohortId);
            String cohortType = cohort.getCohortType();

            Date date = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            if ("0".equals(cohortType)) {
                // cohortType 为 "0" 时，只能安排在周一到周五
                if (!(dayOfWeek.equals(DayOfWeek.MONDAY) || dayOfWeek.equals(DayOfWeek.TUESDAY) ||
                        dayOfWeek.equals(DayOfWeek.WEDNESDAY) || dayOfWeek.equals(DayOfWeek.THURSDAY) ||
                        dayOfWeek.equals(DayOfWeek.FRIDAY))) {
                    clashes++; // 不符合要求的时间段，增加惩罚值
                }
            } else if ("1".equals(cohortType)) {
                // cohortType 为 "1" 时，只能安排在周五和周六
                if (!(dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY))) {
                    clashes++; // 不符合要求的时间段，增加惩罚值
                }
            }
        }

        // 检查节假日
        for (TeachingPlan plan : this.plans) {
            Date scheduledDate = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate scheduledLocalDate = scheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                clashes++; // 在节假日排课，增加较大惩罚值
            }
        }

        return clashes;
    }

    public int calcPenalty() {
        int penalty = 0;

        // 根据 roomId 和 timeslotId 分组
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        // 容量硬约束
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                penalty += 100; // 惩罚值
            }
            if (roomCapacity > 100) { // 大教室惩罚
                penalty += 5;
            }
        }

        // 房间和时间段冲突硬约束
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        penalty += 100; // 惩罚值
                    }
                }
            }
        }

        // 教授时间段冲突硬约束
        Map<Integer, List<TeachingPlan>> professorTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int professorNum = this.getCourse(plan.getCourseId()).getProfessorNum();
            int timeslotId = plan.getTimeslotId();

            if (professorNum >= 1) {
                int key1 = plan.getProfessor1Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key1, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum >= 2) {
                int key2 = plan.getProfessor2Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key2, k -> new ArrayList<>()).add(plan);
            }
            if (professorNum == 3) {
                int key3 = plan.getProfessor3Id() * 1000 + timeslotId;
                professorTimeslotMap.computeIfAbsent(key3, k -> new ArrayList<>()).add(plan);
            }
        }

        // 检查教授时间段冲突
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                penalty += (group.size() - 1) * 100; // 增加冲突的惩罚值
            }
        }

        // 根据 cohortType 限制排课时间段
        for (TeachingPlan plan : this.plans) {
            int cohortId = plan.getCohortId();
            Cohort cohort = this.getCohort(cohortId);
            String cohortType = cohort.getCohortType();

            Date date = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            if ("0".equals(cohortType)) {
                // cohortType 为 "0" 时，只能安排在周一到周五
                if (!(dayOfWeek.equals(DayOfWeek.MONDAY) || dayOfWeek.equals(DayOfWeek.TUESDAY) ||
                        dayOfWeek.equals(DayOfWeek.WEDNESDAY) || dayOfWeek.equals(DayOfWeek.THURSDAY) ||
                        dayOfWeek.equals(DayOfWeek.FRIDAY))) {
                    penalty += 100; // 不符合要求的时间段，增加惩罚值
                }
            } else if ("1".equals(cohortType)) {
                // cohortType 为 "1" 时，只能安排在周五和周六
                if (!(dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY))) {
                    penalty += 100; // 不符合要求的时间段，增加惩罚值
                }
            }
        }

        for (TeachingPlan plan : this.plans) {
            Date scheduledDate = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate scheduledLocalDate = scheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                penalty += 100; // 在节假日排课，增加较大惩罚值
            }
        }

        // 收集所有已排课的时间段（days）
        Set<Integer> scheduledDays = new HashSet<>();
        for (TeachingPlan plan : this.plans) {
            scheduledDays.add(plan.getTimeslotId());
        }
        // 将时间段转换为列表并排序
        List<Integer> sortedDays = new ArrayList<>(scheduledDays);
        Collections.sort(sortedDays);
        // 检查时间段之间的间隔
        for (int i = 1; i < sortedDays.size(); i++) {
            int gap = sortedDays.get(i) - sortedDays.get(i - 1);
            if (gap > 7) {
                penalty += 5; // 或者根据需要调整惩罚值
            }
        }

        return penalty;
    }

    private Set<Integer> getValidProfessorIds(TeachingPlan plan) {
        Set<Integer> validProfessorIds = new HashSet<>();
        if (plan.getProfessor1Id() != 0) {
            validProfessorIds.add(plan.getProfessor1Id());
        }
        if (plan.getProfessor2Id() != 0) {
            validProfessorIds.add(plan.getProfessor2Id());
        }
        if (plan.getProfessor3Id() != 0) {
            validProfessorIds.add(plan.getProfessor3Id());
        }
        return validProfessorIds;
    }



}