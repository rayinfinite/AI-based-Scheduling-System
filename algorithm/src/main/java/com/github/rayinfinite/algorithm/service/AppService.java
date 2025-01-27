package com.github.rayinfinite.algorithm.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.github.rayinfinite.algorithm.entity.Cohort;
import com.github.rayinfinite.algorithm.entity.Course;
import com.github.rayinfinite.algorithm.entity.OutputData;
import com.github.rayinfinite.algorithm.entity.Timeslot;
import com.github.rayinfinite.algorithm.excel.BaseExcelReader;
import com.github.rayinfinite.algorithm.repository.CourseRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    public void downloadExcel(HttpServletResponse response) throws IOException {
        String fileName = "Course";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Course> courseList = courseRepository.findAll();
        List<OutputData> outputDataList = new ArrayList<>();
        for(Course course : courseList) {
            OutputData output = new OutputData();
            BeanUtils.copyProperties(course, output);
            outputDataList.add(output);
        }
        EasyExcel.write(response.getOutputStream(), OutputData.class).sheet("Course").doWrite(outputDataList);
    }
}
