package com.github.rayinfinite.algorithm.service;

import com.github.rayinfinite.algorithm.entity.Classroom;
import com.github.rayinfinite.algorithm.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAllByOrderByIdAsc();
    }
}
