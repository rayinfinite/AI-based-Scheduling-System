package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.service.AlgorithmService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlgorithmController.class)
class AlgorithmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlgorithmService algorithmService;

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

        when(algorithmService.upload(any())).thenReturn("Upload successful");

        mockMvc.perform(multipart("/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Upload successful"));
    }
} 