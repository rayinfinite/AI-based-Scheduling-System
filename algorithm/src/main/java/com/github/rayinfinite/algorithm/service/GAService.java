package com.github.rayinfinite.algorithm.service;

import com.github.rayinfinite.algorithm.entity.*;
import com.github.rayinfinite.algorithm.ga_course.TeachingPlan;
import com.github.rayinfinite.algorithm.ga_course.Timetable;
import com.github.rayinfinite.algorithm.ga_course.config.GA;
import com.github.rayinfinite.algorithm.ga_course.config.Population;
import com.github.rayinfinite.algorithm.utils.PublicHoliday;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class GAService {
    public List<Course> gap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList,
                            List<Classroom> classroomList) {
        List<Timeslot> filtedTimeslotList = timeslotList.stream().filter(this::checkTimeslot).toList();
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setId(i));
        IntStream.range(0, filtedTimeslotList.size()).forEach(i -> filtedTimeslotList.get(i).setId(i));
        Map<String, List<Course>> cohortCourses = courseList.stream().collect(Collectors.groupingBy(Course::getCohort));
        for (Cohort cohort : cohortList) {
            if (cohortCourses.containsKey(cohort.getName())) {
                int[] courseIds = cohortCourses.get(cohort.getName()).stream()
                        .map(Course::getId)
                        .mapToInt(id -> id)
                        .toArray();
                cohort.setCourseIds(courseIds);
            }
        }
        var teacherMap = getProfessorMap(courseList);
        var courseMap = createMap(courseList, Course::getId);
        var cohortMap = createMap(cohortList, Cohort::getId);
        var timeslotMap = createMap(filtedTimeslotList, Timeslot::getId);
        var classroomMap = createMap(classroomList, Classroom::getId);
        setCourseCohortId(courseList, cohortMap);

        Timetable timetable = new Timetable(courseMap, cohortMap, timeslotMap, classroomMap, teacherMap);
        return getSchedule(timetable);
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
        // 初始化 GA
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

        Map<String, Object> clashes = timetable.calcClashes();
        for (Map.Entry<String, Object> entry : clashes.entrySet()) {
            String clashType = entry.getKey();
            List<TeachingPlan> clashPlans = (List<TeachingPlan>) entry.getValue();

            // 打印冲突类型和对应的冲突计划数量
            log.info("{}: {} clashes", clashType, clashPlans.size());

            // 打印每个冲突的详细信息
            for (TeachingPlan plan : clashPlans) {
                log.info("  - Clash Plan ID: {}, Room ID: {}, Timeslot ID: {}",
                        plan.getPlanId(), plan.getRoomId(), plan.getTimeslotId());
            }
        }

        List<Course> courseList = new ArrayList<>();

        // 生成 List<InputData>
        for (TeachingPlan bestPlan : timetable.getPlans()) {
            int courseId = bestPlan.getCourseId();
            Course course = new Course();
            BeanUtils.copyProperties(timetable.getCourse(courseId), course);

            course.setClassroom(timetable.getRoom(bestPlan.getRoomId()).getName());
            course.setCourseDate(timetable.getTimeslot(bestPlan.getTimeslotId()).getDate());

            courseList.add(course);
        }

        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i + 1));

        return courseList;
    }
}
