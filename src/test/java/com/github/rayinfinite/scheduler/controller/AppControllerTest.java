package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.service.AppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppService appService;

    @Test
    @WithMockUser
    void testUploadExcel() throws Exception {
        // 创建模拟的MultipartFile
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.xlsx",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "test content".getBytes()
        );

        when(appService.upload(any())).thenReturn("Upload successful");

        mockMvc.perform(multipart("/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Upload successful"));
    }

    @Test
    @WithMockUser
    void testGetData() throws Exception {
        Course course = new Course();
        course.setId(1);
        List<Course> courseList = List.of(course);
        
        when(appService.findByCourseDateBetween(any(), any(), any(), any()))
            .thenReturn(courseList);

        mockMvc.perform(get("/data")
                .param("startStr", "2024-03-20")
                .param("endStr", "2024-03-21")
                .param("teachers", "teacher1,teacher2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    @WithMockUser
    void testGetAllTeachers() throws Exception {
        List<String> teachers = Arrays.asList("teacher1", "teacher2");
        
        when(appService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value("teacher1"))
                .andExpect(jsonPath("$.data[1]").value("teacher2"));
    }
} 