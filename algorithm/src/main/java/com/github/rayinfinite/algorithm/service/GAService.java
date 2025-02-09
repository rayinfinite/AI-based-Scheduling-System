package com.github.rayinfinite.algorithm.service;

import com.github.rayinfinite.algorithm.entity.*;
import com.github.rayinfinite.algorithm.ga_course.TeachingPlan;
import com.github.rayinfinite.algorithm.ga_course.Timetable;
import com.github.rayinfinite.algorithm.ga_course.config.GA;
import com.github.rayinfinite.algorithm.ga_course.config.Population;
import com.github.rayinfinite.algorithm.utils.PublicHoliday;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
@Slf4j
@Service
public class GAService {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
    private final List<ClashData> clashInfos = new ArrayList<>();
    private final List<RoomUtilization> roomUtilization = new ArrayList<>();
    private List<Registration> registrations = new ArrayList<>();

    public List<Course> gap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList,
                            List<Classroom> classroomList) {
        List<Timeslot> filtedTimeslotList = timeslotList.stream().filter(this::checkTimeslot).toList();
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setId(i));
        IntStream.range(0, filtedTimeslotList.size()).forEach(i -> filtedTimeslotList.get(i).setId(i));

        Map<String, List<Course>> cohortCourses = courseList.stream().collect(Collectors.groupingBy(Course::getCohort));

        // Use Set to Avoid Adding Registration Duplicates
        Set<Registration> registrationsSet = new HashSet<>();

        for (Cohort cohort : cohortList) {
            if (cohortCourses.containsKey(cohort.getName())) {
                int[] courseIds = cohortCourses.get(cohort.getName()).stream()
                        .map(Course::getId)
                        .mapToInt(id -> id)
                        .toArray();
                cohort.setCourseIds(courseIds);
                String cohortName = cohort.getName();
                int cohortSize = cohort.getCohortSize();
                for (Course course : cohortCourses.get(cohort.getName())) {
                    String courseName = course.getCourseName();
                    registrationsSet.add(new Registration(cohortName, cohortSize, courseName));
                }
            }
        }

        var teacherMap = getProfessorMap(courseList);
        var courseMap = createMap(courseList, Course::getId);
        var cohortMap = createMap(cohortList, Cohort::getId);
        var timeslotMap = createMap(filtedTimeslotList, Timeslot::getId);
        var classroomMap = createMap(classroomList, Classroom::getId);
        setCourseCohortId(courseList, cohortMap);

        Timetable timetable = new Timetable(courseMap, cohortMap, timeslotMap, classroomMap, teacherMap);

        // Converting a Set back to a List
        registrations = new ArrayList<>(registrationsSet);

        return getSchedule(timetable);
    }

    public List<OutputData> detection(List<OutputData> dataList, List<Classroom> classroomList) {
        // Extracts and transforms OutputData into base entities
        List<Course> courseList = dataList.stream().map(this::convertToCourse).toList();
        List<Cohort> cohortList = dataList.stream().map(this::convertToCohort).toList();
        List<Timeslot> timeslotList = dataList.stream().map(this::convertToTimeslot).toList();

        // Creating ID Mapping
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setId(i));
        IntStream.range(0, timeslotList.size()).forEach(i -> timeslotList.get(i).setId(i));
        IntStream.range(0, classroomList.size()).forEach(i -> classroomList.get(i).setId(i));

        // Convert to TeachingPlan list
        List<TeachingPlan> teachingPlans = new ArrayList<>();
        Map<Integer, Set<LocalDate>> roomUsageDays = new HashMap<>(); // Record of classroom usage dates
        final LocalDate[] minDate = {null};
        final LocalDate[] maxDate = {null};

        IntStream.range(0, dataList.size()).forEach(i -> {
            OutputData data = dataList.get(i);
            TeachingPlan plan = new TeachingPlan(i,
                    findCohortId(data.getCohort(), cohortList),
                    findCourseId(data.getCourseName(), data.getCourseCode(), courseList));

            plan.addTimeslot(findTimeslotId(data.getCourseDate(), timeslotList));
            plan.setRoomId(findClassroomId(data.getClassroom(), classroomList));
            addProfessorsToPlan(plan, data);

            LocalDate courseDate = data.getCourseDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            roomUsageDays.computeIfAbsent(plan.getRoomId(), k -> new HashSet<>()).add(courseDate);

            // Updated earliest and latest dates
            if (minDate[0] == null || courseDate.isBefore(minDate[0])) {
                minDate[0] = courseDate;
            }
            if (maxDate[0] == null || courseDate.isAfter(maxDate[0])) {
                maxDate[0] = courseDate;
            }

            teachingPlans.add(plan);
        });

        // Creating a Timetable Instance and Calculating Conflicts
        Map<Integer, Course> courseMap = createMap(courseList, Course::getId);
        Map<Integer, Cohort> cohortMap = createMap(cohortList, Cohort::getId);
        Map<Integer, Timeslot> timeslotMap = createMap(timeslotList, Timeslot::getId);
        Map<Integer, Classroom> classroomMap = createMap(classroomList, Classroom::getId);
        var teacherMap = getProfessorMap(courseList);

        Timetable timetable = new Timetable(courseMap, cohortMap, timeslotMap, classroomMap, teacherMap);
        timetable.setPlans(teachingPlans.toArray(new TeachingPlan[0]));

        Map<String, Object> clashes = timetable.calcClashes();
        clashInfos.clear();
        clashes.forEach((clashType, clashDetails) -> {
            List<TeachingPlan> clashPlans = (List<TeachingPlan>) clashDetails;
            int i = 0;
            for (TeachingPlan plan : clashPlans) {
                String roomName = timetable.getRoom(plan.getRoomId()).getName();
                Date dateTime = timetable.getTimeslot(plan.getTimeslotId()).getDate();
                String date = formatter.format(dateTime);
                if (i == 0) {
                    clashInfos.add(new ClashData(clashType, clashPlans.size(), roomName, date));
                    i++;
                } else {
                    clashInfos.add(new ClashData(clashType, null, roomName, date));
                }
            }
        });

        // Calculation of classroom utilisation
        if (minDate[0] != null && maxDate[0] != null) {
            long totalDays = ChronoUnit.DAYS.between(minDate[0], maxDate[0]) + 1;
            long totalUsedDays = 0;
            int totalRooms = roomUsageDays.size();

            for (Map.Entry<Integer, Set<LocalDate>> entry : roomUsageDays.entrySet()) {
                int roomId = entry.getKey();
                int usedDays = entry.getValue().size();
                totalUsedDays += usedDays;
                double utilizationRate = (double) usedDays / totalDays;

                String roomName = timetable.getRoom(roomId).getName();
                String formattedUtilizationRate = String.format("%.2f", utilizationRate * 100) + "%";
                roomUtilization.add(new RoomUtilization(roomName, usedDays, formattedUtilizationRate));
            }

            // Calculation of the total utilisation rate
            double overallUtilizationRate = (double) totalUsedDays / (totalRooms * totalDays);
            String formattedOverallUtilizationRate = String.format("%.2f", overallUtilizationRate * 100) + "%";
            roomUtilization.add(new RoomUtilization("Total", (int) totalDays, formattedOverallUtilizationRate));
        }

        return dataList;
    }

    private Course convertToCourse(OutputData data) {
        Course course = new Course();
        BeanUtils.copyProperties(data, course);
        return course;
    }

    private Cohort convertToCohort(OutputData data) {
        Cohort cohort = new Cohort();
        cohort.setName(data.getCohort());
        cohort.setCohortSize(data.getRun());
        return cohort;
    }

    private Timeslot convertToTimeslot(OutputData data) {
        Timeslot timeslot = new Timeslot();
        timeslot.setDate(data.getCourseDate());
        return timeslot;
    }

    private int findCohortId(String cohortName, List<Cohort> cohortList) {
        return cohortList.stream()
                .filter(c -> c.getName().equals(cohortName))
                .findFirst()
                .map(Cohort::getId)
                .orElse(-1);
    }

    private int findCourseId(String courseName, String courseCode, List<Course> courseList) {
        return courseList.stream()
                .filter(c -> c.getCourseName().equals(courseName) && c.getCourseCode().equals(courseCode))
                .findFirst()
                .map(Course::getId)
                .orElse(-1);
    }


    private int findTimeslotId(Date courseDate, List<Timeslot> timeslotList) {
        return timeslotList.stream()
                .filter(t -> t.getDate().equals(courseDate))
                .findFirst()
                .map(Timeslot::getId)
                .orElse(-1);
    }

    private int findClassroomId(String classroomName, List<Classroom> classroomList) {
        return classroomList.stream()
                .filter(r -> r.getName().equals(classroomName))
                .findFirst()
                .map(Classroom::getId)
                .orElse(-1);
    }

    private void addProfessorsToPlan(TeachingPlan plan, OutputData data) {
        if (data.getTeacher1() != null) plan.addProfessor1(getProfessorId(data.getTeacher1()));
        if (data.getTeacher2() != null) plan.addProfessor2(getProfessorId(data.getTeacher2()));
        if (data.getTeacher3() != null) plan.addProfessor3(getProfessorId(data.getTeacher3()));
    }

    private int getProfessorId(String professorName) {

        return professorName.hashCode();
    }

    public boolean checkTimeslot(Timeslot timeslot) {
        Date date = timeslot.getDate();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !PublicHoliday.isPublicHoliday(localDate);
    }

    private <T> Map<Integer, T> createMap(List<T> list, Function<T, Integer> keyExtractor) {
        return list.stream().collect(Collectors.toMap(keyExtractor, Function.identity()));
    }

    private void setCourseCohortId(List<Course> courseList, Map<Integer, Cohort> cohortMap) {
        Map<String, Integer> invertedCohort = new HashMap<>();
        for (var entry : cohortMap.entrySet()) {
            invertedCohort.put(entry.getValue().getName(), entry.getKey());
        }
        for (var course : courseList) {
            course.setCohortId(invertedCohort.getOrDefault(course.getCohort(), -1));
            if (course.getCohortId() == -1) {
                log.warn("Cohort not found for course: {}", course.getCourseName());
            }
        }
    }

    private Map<Integer, Professor> getProfessorMap(List<Course> courseList) {
        int i = 0;
        Map<String, Integer> teacherMap = new HashMap<>();
        for (Course course : courseList) {
            List<Integer> teachers = new ArrayList<>();
            List<String> teacherNames = Stream.of(course.getTeacher1(), course.getTeacher2(), course.getTeacher3())
                    .filter(name -> name != null && !name.isEmpty()).toList();
            course.setProfessorNum(teacherNames.size());
            for (String teacherName : teacherNames) {
                if (teacherMap.containsKey(teacherName)) {
                    teachers.add(teacherMap.get(teacherName));
                } else {
                    teachers.add(i);
                    teacherMap.put(teacherName, i++);
                }
            }
            course.setTeacherIds(teachers.stream().mapToInt(Integer::intValue).toArray());
        }
        return teacherMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue,
                entry -> new Professor(entry.getValue(), entry.getKey())));
    }

    public List<Course> getSchedule(Timetable timetable) {
        // Initialising GA
        int maxGenerations = 1000;
        GA ga = new GA(100, 0.01, 0.7, 1, 5);
        Population population = ga.initPopulation(timetable);
        int generation = 1;

        while (!ga.isTerminationConditionMet1(population) &&
                !ga.isTerminationconditionMet2(generation, maxGenerations)) {
            population = ga.crossoverPopulation(population);
            population = ga.mutatePopulation(population, timetable);
            ga.evalPopulation(population, timetable);
            generation++;
        }

        timetable.createPlans(population.getFittest(0));
        log.info("Solution found in {} generations", generation);
        log.info("Final solution fitness: {}", population.getFittest(0).getFitness());
//        log.info("Clashes: {}", timetable.calcClashes());

        clashInfos.clear();
        roomUtilization.clear();
        Map<String, Object> clashes = timetable.calcClashes();
        for (Map.Entry<String, Object> entry : clashes.entrySet()) {
            String clashType = entry.getKey();
            List<TeachingPlan> clashPlans = (List<TeachingPlan>) entry.getValue();

            // Print the type of conflict and the corresponding number of conflict plans
//            log.info("{}: {} clashes", clashType, clashPlans.size());

            int i = 0;
            // Print the details of each conflict
            for (TeachingPlan plan : clashPlans) {
                String roomName = timetable.getRoom(plan.getRoomId()).getName();
                Date dateTime = timetable.getTimeslot(plan.getTimeslotId()).getDate();
                String date = formatter.format(dateTime);
//                log.info("Clash at Room: {}, Date: {}",
//                        roomName, Date);
                if (i == 0) {
                    clashInfos.add(new ClashData(clashType, clashPlans.size(), roomName, date));
                    i++;
                } else {
                    clashInfos.add(new ClashData(clashType, null, roomName, date));
                }
            }
        }

        List<Course> courseList = new ArrayList<>();

        Map<String, Map<Integer, List<Course>>> groupedCourses = new HashMap<>();
        Map<Integer, Set<LocalDate>> roomUsageDays = new HashMap<>(); // Record of classroom usage dates
        LocalDate minDate = null;
        LocalDate maxDate = null;

        // Generate List<InputData>
        for (TeachingPlan bestPlan : timetable.getPlans()) {
            int courseId = bestPlan.getCourseId();
            Course course = new Course();
            BeanUtils.copyProperties(timetable.getCourse(courseId), course);

            // Setting additional properties
            course.setClassroom(timetable.getRoom(bestPlan.getRoomId()).getName());
            LocalDate courseDate = timetable.getTimeslot(bestPlan.getTimeslotId())
                    .getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            course.setCourseDate(Date.from(courseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            // Updated records of classroom usage dates
            roomUsageDays.computeIfAbsent(bestPlan.getRoomId(), k -> new HashSet<>()).add(courseDate);

            // Updated earliest and latest dates
            if (minDate == null || courseDate.isBefore(minDate)) {
                minDate = courseDate;
            }
            if (maxDate == null || courseDate.isAfter(maxDate)) {
                maxDate = courseDate;
            }

            // Grouping based on courseName and courseCode, with cohort breakdowns
            String groupKey = course.getCourseName() + "_" + course.getCourseCode();
            int cohortId = timetable.getCohort(bestPlan.getCohortId()).getId();

            groupedCourses
                    .computeIfAbsent(groupKey, k -> new HashMap<>()) // Grouping: identification by course
                    .computeIfAbsent(cohortId, k -> new ArrayList<>())
                    .add(course);

            courseList.add(course);
        }

        // Setting the run value
        for (Map<Integer, List<Course>> cohortGroup : groupedCourses.values()) {
            // Sort cohort in chronological order
            List<Integer> sortedCohorts = cohortGroup.keySet().stream()
                    .sorted((cohort1, cohort2) -> {
                        // Sort by date of cohort's first class
                        Date date1 = cohortGroup.get(cohort1).getFirst().getCourseDate();
                        Date date2 = cohortGroup.get(cohort2).getFirst().getCourseDate();
                        return date1.compareTo(date2);
                    })
                    .toList();

            // Setting run values sequentially
            int run = 1;
            for (int cohortId : sortedCohorts) {
                for (Course course : cohortGroup.get(cohortId)) {
                    course.setRun(run); // Set the same run value for all courses in the cohort
                }
                run++;
            }
        }

        // Calculation of classroom utilisation
        if (minDate != null && maxDate != null) {
            long totalDays = ChronoUnit.DAYS.between(minDate, maxDate) + 1;
//            log.info("Total scheduling days: {}", totalDays);

            long totalUsedDays = 0;
            int totalRooms = roomUsageDays.size();

            for (Map.Entry<Integer, Set<LocalDate>> entry : roomUsageDays.entrySet()) {
                int roomId = entry.getKey();
                int usedDays = entry.getValue().size();
                totalUsedDays += usedDays;
                double utilizationRate = (double) usedDays / totalDays; // Current classroom utilisation rate

                String roomName = timetable.getRoom(roomId).getName();
                String formattedUtilizationRate = String.format("%.2f", utilizationRate * 100) + "%";
//                log.info("Room: {}, Used Days: {}, Total Days: {}, Utilization Rate: {}",
//                        roomName, usedDays, totalDays, formattedUtilizationRate);
                roomUtilization.add(new RoomUtilization(roomName, usedDays, formattedUtilizationRate));
            }

            // Calculation of the total utilisation rate
            double overallUtilizationRate = (double) totalUsedDays / (totalRooms * totalDays);
            String formattedOverallUtilizationRate = String.format("%.2f", overallUtilizationRate * 100) + "%";
            String total = "Total";
            // Total print utilisation
//            log.info("Overall Utilization Rate: {}", formattedOverallUtilizationRate);
            roomUtilization.add(new RoomUtilization(total, (int) totalDays, formattedOverallUtilizationRate));
        }
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i + 1));
        return courseList;
    }

    public void updateRegistrations(List<Registration> newRegistrations) {
        synchronized (this) {
            registrations.clear();
            registrations.addAll(newRegistrations);
        }
    }
}