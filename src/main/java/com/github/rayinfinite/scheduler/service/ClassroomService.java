package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
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

    public Classroom updateClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Classroom deleteClassroom(Integer id) {
        Classroom classroom = classroomRepository.findById(id).orElse(null);
        if (classroom != null) {
            classroomRepository.delete(classroom);
        }
        return classroom;
    }
}
