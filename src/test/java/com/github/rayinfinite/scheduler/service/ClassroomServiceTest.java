package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.repository.ClassroomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomService classroomService;

    @Test
    void testGetAllClassrooms() {
        // Preparing Test Data
        Classroom classroom1 = new Classroom();
        classroom1.setId(1);
        classroom1.setName("Room 1");

        Classroom classroom2 = new Classroom();
        classroom2.setId(2);
        classroom2.setName("Room 2");

        List<Classroom> classrooms = Arrays.asList(classroom1, classroom2);

        // Setting the mock behaviour
        when(classroomRepository.findAllByOrderByIdAsc()).thenReturn(classrooms);

        // execute a test
        List<Classroom> result = classroomService.getAllClassrooms();

        // Verification results
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Room 1");
        assertThat(result.get(1).getName()).isEqualTo("Room 2");
        verify(classroomRepository).findAllByOrderByIdAsc();
    }

    @Test
    void testUpdateClassroom() {
        // Preparing Test Data
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("Updated Room");

        // Setting up mock behaviour
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // execute a test
        Classroom result = classroomService.updateClassroom(classroom);

        // Verification results
        assertThat(result.getName()).isEqualTo("Updated Room");
        verify(classroomRepository).save(classroom);
    }

    @Test
    void testDeleteClassroomWhenExists() {
        // Preparing Test Data
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("Room to Delete");

        // Setting the mock behaviour
        when(classroomRepository.findById(1)).thenReturn(Optional.of(classroom));

        // execute a test
        Classroom result = classroomService.deleteClassroom(1);

        // Verification results
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Room to Delete");
        verify(classroomRepository).findById(1);
        verify(classroomRepository).delete(classroom);
    }

    @Test
    void testDeleteClassroomWhenNotExists() {
        // Setting the mock behaviour
        when(classroomRepository.findById(1)).thenReturn(Optional.empty());

        // execute a test
        Classroom result = classroomService.deleteClassroom(1);

        // Verification results
        assertThat(result).isNull();
        verify(classroomRepository).findById(1);
        verify(classroomRepository, never()).delete(any());
    }
} 