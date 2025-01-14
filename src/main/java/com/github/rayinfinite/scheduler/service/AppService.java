package com.github.rayinfinite.scheduler.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.entity.Timeslot;
import com.github.rayinfinite.scheduler.excel.BaseExcelReader;
import com.github.rayinfinite.scheduler.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    private final CourseRepository courseRepository;
    private final ClassroomService classroomService;
    private final GAService gaService;
    private final Lock lock = new ReentrantLock();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public String upload(MultipartFile file) throws IOException {
        lock.lock();
        log.info("Uploading file: {}", file.getOriginalFilename());
        BaseExcelReader<Course> courseReader = new BaseExcelReader<>();
        BaseExcelReader<Cohort> cohortReader = new BaseExcelReader<>();
        BaseExcelReader<Timeslot> timeReader = new BaseExcelReader<>();
        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {
            ReadSheet courseSheet =
                    EasyExcel.readSheet(0).head(Course.class).registerReadListener(courseReader).build();
            ReadSheet cohortSheet =
                    EasyExcel.readSheet(1).head(Cohort.class).registerReadListener(cohortReader).build();
            ReadSheet timeSheet = EasyExcel.readSheet(2).head(Timeslot.class).registerReadListener(timeReader).build();
            excelReader.read(courseSheet, cohortSheet, timeSheet);
        }
        lock.unlock();

        Thread.ofVirtual().start(() -> gap(courseReader.getDataList(), cohortReader.getDataList(),
                timeReader.getDataList()));
        return "success";
    }

    public void gap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList) {
        var result = gaService.gap(courseList, cohortList, timeslotList, classroomService.getAllClassrooms());
        courseRepository.deleteAll();
        courseRepository.saveAll(result);
        log.info("{} Data saved to database", result.size());
    }

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
