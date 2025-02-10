package com.github.rayinfinite.scheduler.ga_course;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.ga_course.config.Individual;
import com.github.rayinfinite.scheduler.utils.PublicHoliday;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Getter
public class Timetable {
    private final Map<Integer, Classroom> rooms;
    private final Map<Integer, Course> courses;
    private final Map<Integer, Cohort> cohorts;
    private final Map<Integer, Timeslot> timeslots;
    private final Map<Integer, Professor> professors;
    private final Random random = new Random();
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
                Set<Integer> usedTimeslotIds = new HashSet<>();
                for (int dayOffset = 0; dayOffset < duration; dayOffset++) {
                    plans[planIndex] = new TeachingPlan(planIndex, cohort.getId(), course.getId());
                    int timeslotId = chromosome[chromosomePos] + dayOffset;

                    while (timeslotId <= this.getMaxTimeslotId()) {
                        Timeslot timeslot = this.getTimeslot(timeslotId);
                        int dayOfWeek = getDayOfWeekFromDate(timeslot.getDate());

                        LocalDate scheduledLocalDate =
                                timeslot.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        // 根据 cohortType 检查 timeslot 是否合适
                        if (("0".equals(cohortType) && (dayOfWeek == 6 || dayOfWeek == 0)) ||
                                ("1".equals(cohortType) && (dayOfWeek >= 1 && dayOfWeek <= 4 || dayOfWeek == 0)) ||
                                usedTimeslotIds.contains(timeslotId) || PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                            timeslotId++; // 跳到下一个 timeslot
                        } else {
                            break; // 找到合适的 timeslotId，退出循环
                        }
                    }

                    if (timeslotId > this.getMaxTimeslotId()) {
                        timeslotId = chromosome[chromosomePos];
                    }

                    usedTimeslotIds.add(timeslotId); // 记录当前课程的已使用 timeslotId
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
        return (Classroom) roomsArray[(random.nextInt(roomsArray.length))];
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
        return this.timeslots.get(Math.min(timeslotId, getMaxTimeslotId()));
    }


    public int getPlansNum(Timetable timetable) {
        return timetable.getCourses().size();
    }

    public Map<String, Object> calcClashes() {
        Map<String, Object> clashCategories = new HashMap<>();

        clashCategories.put("Room Capacity", new ArrayList<TeachingPlan>());
        clashCategories.put("Room Timeslot Conflict", new ArrayList<TeachingPlan>());
        clashCategories.put("Professor Timeslot Conflict", new ArrayList<TeachingPlan>());
        clashCategories.put("Cohort Time Restriction", new ArrayList<TeachingPlan>());
        clashCategories.put("Public Holiday Conflict", new ArrayList<TeachingPlan>());

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
                ((List<TeachingPlan>) clashCategories.get("Room Capacity")).add(plan);
            }
        }

        // 检查房间与时间段的冲突
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        ((List<TeachingPlan>) clashCategories.get("Room Timeslot Conflict")).add(planA);
                        ((List<TeachingPlan>) clashCategories.get("Room Timeslot Conflict")).add(planB);
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
                for (TeachingPlan plan : group) {
                    ((List<TeachingPlan>) clashCategories.get("Professor Timeslot Conflict")).add(plan);
                }
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
                    ((List<TeachingPlan>) clashCategories.get("Cohort Time Restriction")).add(plan);
                }
            } else if ("1".equals(cohortType)) {
                // cohortType 为 "1" 时，只能安排在周五和周六
                if (!(dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY))) {
                    ((List<TeachingPlan>) clashCategories.get("Cohort Time Restriction")).add(plan);
                }
            }
        }
        // 检查节假日
        for (TeachingPlan plan : this.plans) {
            Date scheduledDate = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate scheduledLocalDate = scheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                ((List<TeachingPlan>) clashCategories.get("Public Holiday Conflict")).add(plan);
            }
        }

        return clashCategories;
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

    private int getDayOfWeekFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY ? 0 : dayOfWeek - 1; // 将SUNDAY映射为0，其他为1-6
    }

    public Timeslot getRandomWeekdayTimeslot() {
        List<Timeslot> weekdaySlots = this.timeslots.values().stream()
                .filter(slot -> {
                    LocalDate localDate = slot.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY; // 过滤工作日
                })
                .toList();
        return weekdaySlots.get(random.nextInt(weekdaySlots.size()));
    }

    public Timeslot getRandomFridaySaturdayTimeslot() {
        List<Timeslot> fridaySaturdaySlots = this.timeslots.values().stream()
                .filter(slot -> {
                    LocalDate localDate = slot.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    return dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY; // 过滤周五和周六
                })
                .toList();
        return fridaySaturdaySlots.get(random.nextInt(fridaySaturdaySlots.size()));
    }

    public void setPlans(TeachingPlan[] plans) {
        this.plans = plans;
        this.plansNum = plans.length;
    }
}