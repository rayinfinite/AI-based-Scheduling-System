package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.*;
import com.github.rayinfinite.scheduler.repository.CourseRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlgorithmServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ClassroomService classroomService;

    @Mock
    private GAService gaService;

    @InjectMocks
    private AlgorithmService algorithmService;

    @Test
    void testUpload() throws Exception {
        // Creating the contents of a valid Excel file
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            ExcelGenerator.createTestExcel()
        );

        // execute a test
        String result = algorithmService.upload(file);

        // Verification results
        assertThat(result).isEqualTo("success");
    }

    @Test
    void testGap() {
        // Preparing Test Data
        List<Course> courseList = List.of(new Course());
        List<Cohort> cohortList = List.of(new Cohort());
        List<Timeslot> timeslotList = List.of(new Timeslot());
        List<Course> resultList = List.of(new Course());

        // cSetting up mock behaviour
        when(gaService.gap(any(), any(), any(), any())).thenReturn(resultList);

        // execute a test
        algorithmService.gap(courseList, cohortList, timeslotList);

        // verify a call
        verify(courseRepository).deleteAll();
        verify(courseRepository).saveAll(resultList);
        verify(gaService).gap(eq(courseList), eq(cohortList), eq(timeslotList), any());
    }

    // Auxiliary method: creating an Excel file for testing
    private static class ExcelGenerator {
        public static byte[] createTestExcel() throws IOException {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Sheet1");

                // Creating Table Headers
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Course Code");
                headerRow.createCell(1).setCellValue("Course Name");
                headerRow.createCell(2).setCellValue("Duration");

                // Creating data rows
                Row dataRow = sheet.createRow(1);
                dataRow.createCell(0).setCellValue("CS101");
                dataRow.createCell(1).setCellValue("Intro to CS");
                dataRow.createCell(2).setCellValue(120);

                // Write to byte array
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    workbook.write(bos);
                    return bos.toByteArray();
                }
            }
        }
    }
} 