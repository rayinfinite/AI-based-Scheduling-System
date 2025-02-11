package com.github.rayinfinite.scheduler.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.excel.BaseExcelReader;
import com.github.rayinfinite.scheduler.repository.CourseRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmService {
    private final CourseRepository courseRepository;
    private final ClassroomService classroomService;
    private final GAService gaService;
    private final Lock lock = new ReentrantLock();
    @Getter
    private boolean taskRunning = false;

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

        Thread.ofVirtual().start(() -> {
            try {
                taskRunning = true;
                gap(courseReader.getDataList(), cohortReader.getDataList(), timeReader.getDataList());
            } finally {
                taskRunning = false;
            }
        });
        return "success";
    }

    public String detectionUpload(MultipartFile file) throws IOException {
        lock.lock();
        log.info("Uploading file: {}", file.getOriginalFilename());

        BaseExcelReader<OutputData> outputDataReader = new BaseExcelReader<>();
        BaseExcelReader<Registration> registrationReader = new BaseExcelReader<>();

        try (ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build()) {
            ReadSheet outputDataSheet =
                    EasyExcel.readSheet(0).head(OutputData.class).registerReadListener(outputDataReader).build();
            ReadSheet registrationSheet =
                    EasyExcel.readSheet(3).head(Registration.class).registerReadListener(registrationReader).build();

            excelReader.read(outputDataSheet, registrationSheet);
        }
        gaService.updateRegistrations(registrationReader.getDataList());
        lock.unlock();

        Thread.ofVirtual().start(() -> {
            try {
                taskRunning = true;
                detection(outputDataReader.getDataList());
            } finally {
                taskRunning = false;
            }
        });
        return "success";
    }


    public void detection(List<OutputData> dataList) {
        var result = gaService.detection(dataList, classroomService.getAllClassrooms());
        List<Course> courseList = new ArrayList<>();
        int i = 1;
        for (OutputData output : result) {
            Course course = new Course();
            BeanUtils.copyProperties(output, course);
            course.setId(i++);
            courseList.add(course);
        }
        courseRepository.deleteAll();
        courseRepository.saveAll(courseList);
        log.info("{} Data saved to database", courseList.size());
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
        for (Course course : courseList) {
            OutputData output = new OutputData();
            BeanUtils.copyProperties(course, output);
            outputDataList.add(output);
        }

        List<ClashData> clashInfos = gaService.getClashInfos();
        List<RoomUtilization> roomUtilizations = gaService.getRoomUtilization();
        List<Registration> registrations = gaService.getRegistrations();

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        // Write First Sheet - Course Information
        WriteSheet courseSheet = EasyExcel.writerSheet(0, "Course").head(OutputData.class).build();
        excelWriter.write(outputDataList, courseSheet);

        // Write to Second Sheet - Conflict Report
        WriteSheet clashSheet = EasyExcel.writerSheet(1, "Clash").head(ClashData.class).build();
        excelWriter.write(clashInfos, clashSheet);

        // Write to Third Sheet - Classroom Utilisation
        WriteSheet roomUtilizationSheet = EasyExcel.writerSheet(2, "Utilization").head(RoomUtilization.class).build();
        excelWriter.write(roomUtilizations, roomUtilizationSheet);

        // Write to 4th Sheet - Register
        WriteSheet registrationSheet = EasyExcel.writerSheet(3, "Registration").head(Registration.class).build();
        excelWriter.write(registrations, registrationSheet);

        // Close ExcelWriter
        excelWriter.finish();
    }
}
