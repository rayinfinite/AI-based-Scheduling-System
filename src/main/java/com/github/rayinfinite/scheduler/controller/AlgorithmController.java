package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.AlgorithmService;
import com.github.rayinfinite.scheduler.utils.LogAction;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AlgorithmController {
    private final AlgorithmService service;

    @LogAction
    @PostMapping("/upload")
    public Response getExcel(MultipartFile file) throws IOException {
        String result = service.upload(file);
        return Response.builder().data(result).build();
    }

    @LogAction
    @PostMapping("/resultUpload")
    public Response getResultExcel(MultipartFile file) throws IOException {
        String result = service.detectionUpload(file);
        return Response.builder().data(result).build();
    }

    @LogAction("null")
    @GetMapping("/download")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        service.downloadExcel(response);
    }
}
