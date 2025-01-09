package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.service.AppService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AppController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AppControllerDiffblueTest {
    @Autowired
    private AppController appController;

    @MockitoBean
    private AppService appService;

    /**
     * Test {@link AppController#getExcel(MultipartFile)}.
     * <p>
     * Method under test: {@link AppController#getExcel(MultipartFile)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile)")
    void testGetExcel() throws Exception {
        // Arrange
        when(appService.upload(Mockito.any())).thenReturn("Upload");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/upload");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":null,\"message\":null," +
                        "\"data\":\"Upload\"}"));
    }

    /**
     * Test {@link AppController#getData(String, String, List)}.
     * <p>
     * Method under test: {@link AppController#getData(String, String, List)}
     */
    @Test
    @DisplayName("Test getData(String, String, List)")
    void testGetData() throws Exception {
        // Arrange
        when(appService.findByCourseDateBetween(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/data")
                .param("endStr", "foo")
                .param("startStr", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }

    /**
     * Test {@link AppController#getAllTeachers()}.
     * <ul>
     *   <li>Given {@code /teacher}.</li>
     *   <li>When formLogin.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getAllTeachers()}
     */
    @Test
    @DisplayName("Test getAllTeachers(); given '/teacher'; when formLogin; then status isNotFound()")
    void testGetAllTeachers_givenTeacher_whenFormLogin_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(appService.getAllTeachers()).thenReturn(new ArrayList<>());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder =
                SecurityMockMvcRequestBuilders.formLogin();

        // Act
        ResultActions actualPerformResult =
                MockMvcBuilders.standaloneSetup(appController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link AppController#getAllTeachers()}.
     * <ul>
     *   <li>When {@link MockMvcRequestBuilders#get(String, Object[])}
     * {@code /teacher}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppController#getAllTeachers()}
     */
    @Test
    @DisplayName("Test getAllTeachers(); when get(String, Object[]) '/teacher'; then status isOk()")
    void testGetAllTeachers_whenGetTeacher_thenStatusIsOk() throws Exception {
        // Arrange
        when(appService.getAllTeachers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/teacher");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(appController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"code\":\"success\",\"message\":\"success\"," +
                                "\"data\":[]}"));
    }
}
