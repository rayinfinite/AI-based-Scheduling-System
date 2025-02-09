package com.github.rayinfinite.algorithm.controller;

import com.github.rayinfinite.algorithm.entity.Response;
import com.github.rayinfinite.algorithm.service.AlgorithmService;
import com.github.rayinfinite.algorithm.utils.LogAction;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping(value = "/upload/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Boolean>> streamTaskStatus() {
        return Flux.interval(Duration.ofSeconds(1)) // 每秒推送一次状态
                .map(sequence -> {
                    Map<String, Boolean> status = new HashMap<>();
                    status.put("running", service.isTaskRunning());
                    return status;
                });
    }
}
