package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.ga_course.TeachingPlan;
import com.github.rayinfinite.scheduler.ga_course.Timetable;
import com.github.rayinfinite.scheduler.ga_course.config.GA;
import com.github.rayinfinite.scheduler.ga_course.config.Population;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class GAService {
    public List<Course> gap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList,
                            List<Classroom> classroomList) {
        IntStream.range(0, courseList.size()).forEach(i -> courseList.get(i).setId(i));
        IntStream.range(0, cohortList.size()).forEach(i -> cohortList.get(i).setId(i));
        IntStream.range(0, timeslotList.size()).forEach(i -> timeslotList.get(i).setId(i));
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
        var timeslotMap = createMap(timeslotList, Timeslot::getId);
        var classroomMap = createMap(classroomList, Classroom::getId);
        setCourseCohortId(courseList, cohortMap);

        Timetable timetable = new Timetable(courseMap, cohortMap, timeslotMap, classroomMap, teacherMap);
        return getSchedule(timetable);
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
        GA ga = new GA(100, 0.001, 0.98, 1, 5);
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
        log.info("Clashes: {}", timetable.calcClashes());

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
