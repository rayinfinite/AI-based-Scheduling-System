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

    //Shallow copy for fitness calculation
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

                        // Check if the timeslot is appropriate according to cohortType.
                        if (("0".equals(cohortType) && (dayOfWeek == 6 || dayOfWeek == 0)) ||
                                ("1".equals(cohortType) && (dayOfWeek >= 1 && dayOfWeek <= 4 || dayOfWeek == 0)) ||
                                usedTimeslotIds.contains(timeslotId) || PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                            timeslotId++; // Skip to next timeslot
                        } else {
                            break; // Find the right timeslotId, exit the loop
                        }
                    }

                    if (timeslotId > this.getMaxTimeslotId()) {
                        timeslotId = chromosome[chromosomePos];
                    }

                    usedTimeslotIds.add(timeslotId); // Records the current course's used timeslotId
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

        // Grouping by roomId and timeslotId
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        // Capacity check: traverse all plans once
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                ((List<TeachingPlan>) clashCategories.get("Room Capacity")).add(plan);
            }
        }

        // Checking room conflicts with time slots
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

        // Examining the Professor's Conflict
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

        // Check for conflicts in professor's time slot
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                for (TeachingPlan plan : group) {
                    ((List<TeachingPlan>) clashCategories.get("Professor Timeslot Conflict")).add(plan);
                }
            }
        }

        // Limit scheduling timeslots based on cohortType
        for (TeachingPlan plan : this.plans) {
            int cohortId = plan.getCohortId();
            Cohort cohort = this.getCohort(cohortId);
            String cohortType = cohort.getCohortType();

            Date date = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            if ("0".equals(cohortType)) {
                // When cohortType is "0", it can only be scheduled from Monday to Friday.
                if (!(dayOfWeek.equals(DayOfWeek.MONDAY) || dayOfWeek.equals(DayOfWeek.TUESDAY) ||
                        dayOfWeek.equals(DayOfWeek.WEDNESDAY) || dayOfWeek.equals(DayOfWeek.THURSDAY) ||
                        dayOfWeek.equals(DayOfWeek.FRIDAY))) {
                    ((List<TeachingPlan>) clashCategories.get("Cohort Time Restriction")).add(plan);
                }
            } else if ("1".equals(cohortType)) {
                // When cohortType is "1", only Fridays and Saturdays can be scheduled.
                if (!(dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY))) {
                    ((List<TeachingPlan>) clashCategories.get("Cohort Time Restriction")).add(plan);
                }
            }
        }
        // Checking holidays
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

        // Grouping by roomId and timeslotId
        Map<Integer, List<TeachingPlan>> roomTimeslotMap = new HashMap<>();
        for (TeachingPlan plan : this.plans) {
            int key = plan.getRoomId() * 1000 + plan.getTimeslotId(); // 简单的键生成方式
            roomTimeslotMap.computeIfAbsent(key, k -> new ArrayList<>()).add(plan);
        }

        // Capacity hard constraints
        for (TeachingPlan plan : this.plans) {
            int roomCapacity = this.getRoom(plan.getRoomId()).getSize();
            int cohortSize = this.getCohort(plan.getCohortId()).getCohortSize();
            if (roomCapacity < cohortSize) {
                penalty += 100; // penalty value
            }
            if (roomCapacity > 100) { // Punishment in a large classroom
                penalty += 5;
            }
        }

        // Hard constraints on room and time slot conflicts
        for (List<TeachingPlan> group : roomTimeslotMap.values()) {
            for (int i = 0; i < group.size(); i++) {
                TeachingPlan planA = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    TeachingPlan planB = group.get(j);
                    if (planA.getPlanId() != planB.getPlanId()) {
                        penalty += 100; // penalty value
                    }
                }
            }
        }

        // Conflict hard constraints on the professor's time period
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

        // Check for conflicts in professor's time slot
        for (List<TeachingPlan> group : professorTimeslotMap.values()) {
            if (group.size() > 1) {
                penalty += (group.size() - 1) * 100; // Increasing the penalty value of a conflict
            }
        }

        // Limit scheduling time slots based on cohortType
        for (TeachingPlan plan : this.plans) {
            int cohortId = plan.getCohortId();
            Cohort cohort = this.getCohort(cohortId);
            String cohortType = cohort.getCohortType();

            Date date = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            if ("0".equals(cohortType)) {
                // When cohortType is "0", it can only be scheduled from Monday to Friday.
                if (!(dayOfWeek.equals(DayOfWeek.MONDAY) || dayOfWeek.equals(DayOfWeek.TUESDAY) ||
                        dayOfWeek.equals(DayOfWeek.WEDNESDAY) || dayOfWeek.equals(DayOfWeek.THURSDAY) ||
                        dayOfWeek.equals(DayOfWeek.FRIDAY))) {
                    penalty += 100; // Increase in penalty value for non-compliant time periods
                }
            } else if ("1".equals(cohortType)) {
                // When cohortType is "1", only Fridays and Saturdays can be scheduled.
                if (!(dayOfWeek.equals(DayOfWeek.FRIDAY) || dayOfWeek.equals(DayOfWeek.SATURDAY))) {
                    penalty += 100;
                }
            }
        }

        for (TeachingPlan plan : this.plans) {
            Date scheduledDate = this.getTimeslot(plan.getTimeslotId()).getDate();
            LocalDate scheduledLocalDate = scheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (PublicHoliday.isPublicHoliday(scheduledLocalDate)) {
                penalty += 100; // Scheduling classes on holidays, adding larger penalty values
            }
        }

        // Collection of all scheduled time slots (days)
        Set<Integer> scheduledDays = new HashSet<>();
        for (TeachingPlan plan : this.plans) {
            scheduledDays.add(plan.getTimeslotId());
        }
        // Convert time periods to lists and sort them
        List<Integer> sortedDays = new ArrayList<>(scheduledDays);
        Collections.sort(sortedDays);
        // Check the interval between time periods
        for (int i = 1; i < sortedDays.size(); i++) {
            int gap = sortedDays.get(i) - sortedDays.get(i - 1);
            if (gap > 7) {
                penalty += 5;
            }
        }

        return penalty;
    }

    private int getDayOfWeekFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY ? 0 : dayOfWeek - 1; // Maps SUNDAY to 0, others to 1-6
    }

    public Timeslot getRandomWeekdayTimeslot() {
        List<Timeslot> weekdaySlots = this.timeslots.values().stream()
                .filter(slot -> {
                    LocalDate localDate = slot.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY; // Filtering workdays
                })
                .toList();
        return weekdaySlots.get(random.nextInt(weekdaySlots.size()));
    }

    public Timeslot getRandomFridaySaturdayTimeslot() {
        List<Timeslot> fridaySaturdaySlots = this.timeslots.values().stream()
                .filter(slot -> {
                    LocalDate localDate = slot.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    return dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY; // Filter Friday and Saturday
                })
                .toList();
        return fridaySaturdaySlots.get(random.nextInt(fridaySaturdaySlots.size()));
    }

    public void setPlans(TeachingPlan[] plans) {
        this.plans = plans;
        this.plansNum = plans.length;
    }
}