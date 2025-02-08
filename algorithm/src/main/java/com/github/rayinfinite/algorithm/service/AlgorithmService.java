package com.github.rayinfinite.algorithm.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.rayinfinite.algorithm.entity.*;
import com.github.rayinfinite.algorithm.excel.BaseExcelReader;
import com.github.rayinfinite.algorithm.repository.CourseRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private final StringRedisTemplate redisTemplate;

    public void evictCaches() {
        // 定义要清除的缓存前缀列表
        String[] cacheNames = {"course", "teacher", "cohort"};

        for (String cacheName : cacheNames) {
            // 使用keys命令匹配所有以cacheName为前缀的key，并删除它们
            Set<String> keys = redisTemplate.keys(cacheName + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    @CacheEvict(value = {"course", "teacher", "cohort"}, allEntries = true)
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

    @CacheEvict(value = {"course", "teacher", "cohort"}, allEntries = true)
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

        Thread.ofVirtual().start(() -> detection(outputDataReader.getDataList()));
        return "success";
    }


    public void detection(List<OutputData> dataList) {
        var result = gaService.detection(dataList, classroomService.getAllClassrooms());
        List<Course> courseList = new ArrayList<>();
        int i = 1;
        for (OutputData output : result) {
            Course course = new Course();
            BeanUtils.copyProperties(course, output);
            course.setId(i++);
            courseList.add(course);
        }
        courseRepository.deleteAll();
        courseRepository.saveAll(courseList);
        log.info("{} Data saved to database", courseList.size());
        evictCaches();
    }

    public void gap(List<Course> courseList, List<Cohort> cohortList, List<Timeslot> timeslotList) {
        var result = gaService.gap(courseList, cohortList, timeslotList, classroomService.getAllClassrooms());
        courseRepository.deleteAll();
        courseRepository.saveAll(result);
        log.info("{} Data saved to database", result.size());
        evictCaches();
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

        // 写入第一个 Sheet - 课程信息
        WriteSheet courseSheet = EasyExcel.writerSheet(0, "Course").head(OutputData.class).build();
        excelWriter.write(outputDataList, courseSheet);

        // 写入第二个 Sheet - 冲突报告
        WriteSheet clashSheet = EasyExcel.writerSheet(1, "Clash").head(ClashData.class).build();
        excelWriter.write(clashInfos, clashSheet);

        // 写入第三个 Sheet - 教室利用率
        WriteSheet roomUtilizationSheet = EasyExcel.writerSheet(2, "Utilization").head(RoomUtilization.class).build();
        excelWriter.write(roomUtilizations, roomUtilizationSheet);

        // 写入第四个 Sheet - 注册
        WriteSheet registrationSheet = EasyExcel.writerSheet(3, "Registration").head(Registration.class).build();
        excelWriter.write(registrations, registrationSheet);

        // 关闭 ExcelWriter
        excelWriter.finish();
    }
}
