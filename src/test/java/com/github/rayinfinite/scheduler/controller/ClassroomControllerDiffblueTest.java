package com.github.rayinfinite.scheduler.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.utils.LoginUtil;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ClassroomController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ClassroomControllerDiffblueTest {
    @Autowired
    private ClassroomController classroomController;

    @MockBean
    private ClassroomService classroomService;

    /**
     * Test {@link ClassroomController#getAllClassrooms()}.
     * <p>
     * Method under test: {@link ClassroomController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms()")
    @Tag("MaintainedByDiffblue")
    void testGetAllClassrooms() throws Exception {
        // Arrange
        when(classroomService.getAllClassrooms()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/classroom");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link ClassroomController#updateClassroom(Classroom)}.
     * <p>
     * Method under test: {@link ClassroomController#updateClassroom(Classroom)}
     */
    @Test
    @DisplayName("Test updateClassroom(Classroom)")
    @Tag("MaintainedByDiffblue")
    void testUpdateClassroom() throws Exception {
        // Arrange
        when(classroomService.updateClassroom(Mockito.<Classroom>any())).thenReturn(new Classroom());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/classroom")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new Classroom()));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"code\":\"success\",\"message\":\"success\",\"data\":{\"id\":null,\"name\":null," +
                                        "\"size\":null,\"software\":null}}"));
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer)}.
     * <ul>
     *   <li>Given {@code /classroom/{id}}.</li>
     *   <li>When formLogin.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ClassroomController#deleteClassroom(Integer)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer); given '/classroom/{id}'; when formLogin; then status isNotFound()")
    @Tag("MaintainedByDiffblue")
    void testDeleteClassroom_givenClassroomId_whenFormLogin_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(classroomService.deleteClassroom(Mockito.<Integer>any())).thenReturn(new Classroom());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder =
                SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ClassroomController#deleteClassroom(Integer)}.
     * <ul>
     *   <li>When one.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ClassroomController#deleteClassroom(Integer)}
     */
    @Test
    @DisplayName("Test deleteClassroom(Integer); when one; then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testDeleteClassroom_whenOne_thenStatusIsOk() throws Exception {
        // Arrange
        when(classroomService.deleteClassroom(Mockito.<Integer>any())).thenReturn(new Classroom());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/classroom/{id}", 1);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(classroomController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"code\":\"success\",\"message\":\"success\",\"data\":{\"id\":null,\"name\":null,"
                                + "\"size\":null,\"software\":null}}"));
    }

}
