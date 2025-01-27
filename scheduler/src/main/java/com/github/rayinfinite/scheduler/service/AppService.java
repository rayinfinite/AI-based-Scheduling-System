package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final CourseRepository courseRepository;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public List<Course> findByCourseDateBetween(String startDate, String endDate, List<String> teachers, List<String> cohorts) throws ParseException {
        Date start = formatter.parse(startDate);
        Date end = formatter.parse(endDate);
        List<Course> data = courseRepository.findByCourseDateBetween(start, end);
        if (teachers != null && !teachers.isEmpty()) {
            data.removeIf(inputData -> !teachers.contains(inputData.getTeacher1())
                    && !teachers.contains(inputData.getTeacher2())
                    && !teachers.contains(inputData.getTeacher3()));
        }
        if(cohorts != null && !cohorts.isEmpty()) {
            data.removeIf(inputData -> !cohorts.contains(inputData.getCohort()));
        }
        return data;
    }

    public List<String> getAllTeachers() {
        List<String> list = new ArrayList<>();
        list.addAll(courseRepository.findTeacher1());
        list.addAll(courseRepository.findTeacher2());
        list.addAll(courseRepository.findTeacher3());
        return list.stream().filter(Objects::nonNull).filter(s -> !s.isEmpty())
                .distinct().sorted().toList();
    }

    public List<String> getAllCohorts() {
        return courseRepository.findCohort();
    }
}
