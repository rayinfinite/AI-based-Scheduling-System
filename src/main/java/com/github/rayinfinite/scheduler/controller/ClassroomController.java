package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService service;

    @GetMapping
    public Response getAllClassrooms() {
        List<Classroom> data = service.getAllClassrooms();
        return new Response(data);
    }

    @PostMapping
    public Response updateClassroom(@RequestBody Classroom classroom) {
        var result = service.updateClassroom(classroom);
        return new Response(result);
    }

    @DeleteMapping("/{id}")
    public Response deleteClassroom(@PathVariable("id") Integer classroomId) {
        var result = service.deleteClassroom(classroomId);
        return new Response(result);
    }
}
