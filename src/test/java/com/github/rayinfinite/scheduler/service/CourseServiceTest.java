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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void testFindByCourseDateBetween() throws Exception {
        // Preparing Test Data
        List<Course> courseList = new ArrayList<>(Arrays.asList(
                createCourse("Teacher1", null, null),
                createCourse("Teacher2", "Teacher3", null)
        ));

        // Setting the mock behaviour
        when(courseRepository.findByCourseDateBetween(any(), any())).thenReturn(courseList);

        // Perform Test - No Teacher Filtering
        List<Course> result1 = courseService.findByCourseDateBetween(
                "2024-03-20T00:00:00.000Z",
                "2024-03-21T00:00:00.000Z",
                null,
                null
        );
        assertThat(result1).hasSize(2);

        // Execute the test - with teacher filtering
        List<Course> result2 = courseService.findByCourseDateBetween(
                "2024-03-20T00:00:00.000Z",
                "2024-03-21T00:00:00.000Z",
                Arrays.asList("Teacher1"),
                null
        );
        assertThat(result2).hasSize(1);
    }

    @Test
    void testGetAllTeachers() {
        // Preparing Test Data
        when(courseRepository.findTeacher1()).thenReturn(Arrays.asList("Teacher1", "Teacher2"));
        when(courseRepository.findTeacher2()).thenReturn(Arrays.asList("Teacher2", "Teacher3"));
        when(courseRepository.findTeacher3()).thenReturn(Arrays.asList("Teacher3", "Teacher4"));

        // execute a test
        List<String> result = courseService.getAllTeachers();

        // Verification results
        assertThat(result)
                .hasSize(4)
                .containsExactly("Teacher1", "Teacher2", "Teacher3", "Teacher4");
    }
    @Test
    void testGetAllCohorts() {
        // Preparing Test Data
        when(courseRepository.findCohort()).thenReturn(Arrays.asList("Cohort1", "Cohort2"));

        // execute a test
        List<String> result = courseService.getAllCohorts();

        // Verification results
        assertThat(result).hasSize(2).containsExactly("Cohort1", "Cohort2");
    }


    private Course createCourse(String teacher1, String teacher2, String teacher3) {
        Course course = new Course();
        course.setTeacher1(teacher1);
        course.setTeacher2(teacher2);
        course.setTeacher3(teacher3);
        return course;
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