package com.github.rayinfinite.scheduler.controller;

import com.github.rayinfinite.scheduler.config.security.UserInfoDetails;
import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.Response;
import com.github.rayinfinite.scheduler.service.ClassroomService;
import com.github.rayinfinite.scheduler.utils.LoginUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/classroom")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService service;
    private final LoginUtil loginUtil;

    @GetMapping
    public Response getAllClassrooms() {
        List<Classroom> data = service.getAllClassrooms();
        return new Response(data);
    }

    @PostMapping
    public Response updateClassroom(@RequestBody Classroom classroom, @AuthenticationPrincipal UserInfoDetails principal) {
        loginUtil.log("POST /classroom", classroom.toString(), principal);
        return updateClassroom(classroom);
    }

    public Response updateClassroom(Classroom classroom) {
        var result = service.updateClassroom(classroom);
        return new Response(result);
    }

    @DeleteMapping("/{id}")
    public Response deleteClassroom(@PathVariable("id") Integer classroomId, @AuthenticationPrincipal UserInfoDetails principal) {
        loginUtil.log("DELETE /classroom", classroomId.toString(), principal);
        return deleteClassroom(classroomId);
    }

    public Response deleteClassroom(Integer classroomId) {
        var result = service.deleteClassroom(classroomId);
        return new Response(result);
    }
}
