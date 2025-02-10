package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CourseController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CourseControllerDiffblueTest {
    @Autowired
    private CourseController algorithmController;
    
    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private ClassroomService classroomService;

    /**
     * Test {@link CourseController#getData(String, String, List, List)}.
     * <p>
     * Method under test: {@link CourseController#getData(String, String, List, List)}
     */
    @Test
    @DisplayName("Test getData(String, String, List, List)")
    void testGetData2() throws Exception {
        // Arrange
        when(courseService.findByCourseDateBetween(Mockito.any(), Mockito.any(),
                Mockito.any(),
                Mockito.any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/data")
                .param("endStr", "foo")
                .param("startStr", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(algorithmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link CourseController#getAllTeachers()}.
     * <p>
     * Method under test: {@link CourseController#getAllTeachers()}
     */
    @Test
    @DisplayName("Test getAllTeachers()")
    void testGetAllTeachers() throws Exception {
        // Arrange
        when(courseService.getAllTeachers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teacher");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(algorithmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link CourseController#getData(String, String, List, List)}.
     * <p>
     * Method under test: {@link CourseController#getData(String, String, List, List)}
     */
    @Test
    @DisplayName("Test getData(String, String, List)")
    void testGetData() throws Exception {
        // Arrange
        when(courseService.findByCourseDateBetween(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/data")
                .param("endStr", "foo")
                .param("startStr", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(algorithmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link CourseController#getAllCohorts()}.
     * <p>
     * Method under test: {@link CourseController#getAllCohorts()}
     */
    @Test
    @DisplayName("Test getAllCohorts()")
    void testGetAllCohorts() throws Exception {
        // Arrange
        when(courseService.getAllCohorts()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/student");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(algorithmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link CourseController#getAllClassrooms()}.
     * <ul>
     *   <li>Given {@code /classname}.</li>
     *   <li>When formLogin.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CourseController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); given '/classname'; when formLogin; then status isNotFound()")
    void testGetAllClassrooms_givenClassname_whenFormLogin_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(classroomService.getAllClassroomNames()).thenReturn(new ArrayList<>());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder =
                SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(algorithmController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CourseController#getAllClassrooms()}.
     * <ul>
     *   <li>When {@link MockMvcRequestBuilders#get(String, Object[])}
     * {@code /classname}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CourseController#getAllClassrooms()}
     */
    @Test
    @DisplayName("Test getAllClassrooms(); when get(String, Object[]) '/classname'; then status isOk()")
    void testGetAllClassrooms_whenGetClassname_thenStatusIsOk() throws Exception {
        // Arrange
        when(classroomService.getAllClassroomNames()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/classname");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(algorithmController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }
}
