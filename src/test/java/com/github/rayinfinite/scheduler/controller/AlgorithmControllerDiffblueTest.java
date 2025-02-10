package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.AlgorithmService;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AlgorithmController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AlgorithmControllerDiffblueTest {
    @Autowired
    private AlgorithmController algorithmController;

    @MockitoBean
    private AlgorithmService algorithmService;

    @MockitoBean
    private ClassroomService classroomService;

    /**
     * Test {@link AlgorithmController#getExcel(MultipartFile)} with
     * {@code file}, {@code principal}.
     * <ul>
     *   <li>Then return Data is {@code Upload}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AlgorithmController#getExcel(MultipartFile)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile) with 'file', 'principal'; then return Data is 'Upload'")
    void testGetExcelWithFilePrincipal_thenReturnDataIsUpload() throws IOException {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        AlgorithmService service = mock(AlgorithmService.class);
        when(service.upload(Mockito.any())).thenReturn("Upload");
        AlgorithmController algorithmController = new AlgorithmController(service);
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("foo.txt");

        // Act
        Response actualExcel = algorithmController.getExcel(file);

        // Assert
        verify(service).upload(isA(MultipartFile.class));
        assertEquals("Upload", actualExcel.getData());
        assertNull(actualExcel.getCode());
        assertNull(actualExcel.getMessage());
    }

    /**
     * Test {@link AlgorithmController#getExcel(MultipartFile)} with {@code file}.
     * <ul>
     *   <li>Given {@link AlgorithmService} {@link AlgorithmService#upload(MultipartFile)} return
     * {@code Upload}.</li>
     *   <li>Then return Data is {@code Upload}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AlgorithmController#getExcel(MultipartFile)}
     */
    @Test
    @DisplayName("Test getExcel(MultipartFile) with 'file'; given AppService upload(MultipartFile) return 'Upload'; "
            + "then return Data is 'Upload'")
    void testGetExcelWithFile_givenAppServiceUploadReturnUpload_thenReturnDataIsUpload() throws IOException {
        // Arrange
        when(algorithmService.upload(Mockito.any())).thenReturn("Upload");

        // Act
        Response actualExcel = algorithmController
                .getExcel(new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));

        // Assert
        verify(algorithmService).upload(isA(MultipartFile.class));
        assertEquals("Upload", actualExcel.getData());
        assertNull(actualExcel.getCode());
        assertNull(actualExcel.getMessage());
    }
}
