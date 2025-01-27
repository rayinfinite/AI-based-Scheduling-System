package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.AppService;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppController {
    private final AppService service;
    private final ClassroomService classroomService;
    private final LoginUtil loginUtil;

    @GetMapping("/data")
    public Response getData(String startStr, String endStr,
                            @RequestParam(required = false) List<String> teachers,
                            @RequestParam(required = false) List<String> students) throws ParseException {
        List<Course> data = service.findByCourseDateBetween(startStr, endStr, teachers, students);
        return new Response(data);
    }

    @GetMapping("/teacher")
    public Response getAllTeachers() {
        List<String> data = service.getAllTeachers();
        return new Response(data);
    }

    @GetMapping("/student")
    public Response getAllCohorts() {
        List<String> data = service.getAllCohorts();
        return new Response(data);
    }

    @GetMapping("/classname")
    public Response getAllClassrooms() {
        List<String> data = classroomService.getAllClassroomNames();
        return new Response(data);
    }
}
