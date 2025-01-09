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
        // 准备测试数据
        Classroom classroom1 = new Classroom();
        classroom1.setId(1);
        classroom1.setName("Room 1");

        Classroom classroom2 = new Classroom();
        classroom2.setId(2);
        classroom2.setName("Room 2");

        List<Classroom> classrooms = Arrays.asList(classroom1, classroom2);

        // 设置 mock 行为
        when(classroomRepository.findAllByOrderByIdAsc()).thenReturn(classrooms);

        // 执行测试
        List<Classroom> result = classroomService.getAllClassrooms();

        // 验证结果
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Room 1");
        assertThat(result.get(1).getName()).isEqualTo("Room 2");
        verify(classroomRepository).findAllByOrderByIdAsc();
    }

    @Test
    void testUpdateClassroom() {
        // 准备测试数据
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("Updated Room");

        // 设置 mock 行为
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // 执行测试
        Classroom result = classroomService.updateClassroom(classroom);

        // 验证结果
        assertThat(result.getName()).isEqualTo("Updated Room");
        verify(classroomRepository).save(classroom);
    }

    @Test
    void testDeleteClassroomWhenExists() {
        // 准备测试数据
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("Room to Delete");

        // 设置 mock 行为
        when(classroomRepository.findById(1)).thenReturn(Optional.of(classroom));

        // 执行测试
        Classroom result = classroomService.deleteClassroom(1);

        // 验证结果
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Room to Delete");
        verify(classroomRepository).findById(1);
        verify(classroomRepository).delete(classroom);
    }

    @Test
    void testDeleteClassroomWhenNotExists() {
        // 设置 mock 行为
        when(classroomRepository.findById(1)).thenReturn(Optional.empty());

        // 执行测试
        Classroom result = classroomService.deleteClassroom(1);

        // 验证结果
        assertThat(result).isNull();
        verify(classroomRepository).findById(1);
        verify(classroomRepository, never()).delete(any());
    }
} 