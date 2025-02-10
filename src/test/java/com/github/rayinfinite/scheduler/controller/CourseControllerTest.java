package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private ClassroomService classroomService;

    @Test
    @WithMockUser
    void testGetData() throws Exception {
        Course course = new Course();
        course.setId(1);
        List<Course> courseList = List.of(course);

        when(courseService.findByCourseDateBetween(any(), any(), any(), any()))
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

        when(courseService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]").value("teacher1"))
                .andExpect(jsonPath("$.data[1]").value("teacher2"));
    }
}
