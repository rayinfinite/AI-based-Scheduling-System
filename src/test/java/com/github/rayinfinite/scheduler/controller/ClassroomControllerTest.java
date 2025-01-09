package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassroomController.class)
class ClassroomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClassroomService classroomService;

    @Test
    @WithMockUser
    void testGetAllClassrooms() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setId(1);
        List<Classroom> classrooms = List.of(classroom);

        when(classroomService.getAllClassrooms()).thenReturn(classrooms);

        mockMvc.perform(get("/classroom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    @WithMockUser
    void testUpdateClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setId(1);

        when(classroomService.updateClassroom(any(Classroom.class))).thenReturn(classroom);

        mockMvc.perform(post("/classroom")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @WithMockUser
    void testDeleteClassroom() throws Exception {
        Classroom classroom = new Classroom();
        classroom.setId(1);
        when(classroomService.deleteClassroom(1)).thenReturn(classroom);

        mockMvc.perform(delete("/classroom/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }
} 