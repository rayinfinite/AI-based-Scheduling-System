package com.github.rayinfinite.algorithm.controller;

import com.github.rayinfinite.algorithm.config.security.UserInfoDetails;
import com.github.rayinfinite.algorithm.entity.Response;
import com.github.rayinfinite.algorithm.service.AppService;
import com.github.rayinfinite.algorithm.utils.LoginUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppController {
    private final AppService service;
    private final LoginUtil loginUtil;

    @PostMapping("/upload")
    public Response getExcel(MultipartFile file, @AuthenticationPrincipal UserInfoDetails principal) throws IOException {
        loginUtil.log("POST /upload", file.getOriginalFilename(), principal);
        return getExcel(file);
    }

    public Response getExcel(MultipartFile file) throws IOException {
        String result = service.upload(file);
        return Response.builder().data(result).build();
    }

    @GetMapping("/download")
    public void downloadExcel(HttpServletResponse response, @AuthenticationPrincipal UserInfoDetails principal) throws IOException {
        loginUtil.log("GET /download", null, principal);
        service.downloadExcel(response);
    }
}
