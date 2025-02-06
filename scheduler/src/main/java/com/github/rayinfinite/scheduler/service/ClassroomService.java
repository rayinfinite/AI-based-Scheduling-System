package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Cacheable("classrooms")
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAllByOrderByIdAsc();
    }

    @Cacheable("classroomNames")
    public List<String> getAllClassroomNames() {
        List<Classroom> classrooms = classroomRepository.findAllByOrderByIdAsc();
        return classrooms.stream().map(Classroom::getName).toList();
    }

    @CacheEvict(value = {"classrooms", "classroomNames"}, allEntries = true)
    public Classroom updateClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @CacheEvict(value = {"classrooms", "classroomNames"}, allEntries = true)
    public Classroom deleteClassroom(Integer id) {
        Classroom classroom = classroomRepository.findById(id).orElse(null);
        if (classroom != null) {
            classroomRepository.delete(classroom);
        }
        return classroom;
    }
}
