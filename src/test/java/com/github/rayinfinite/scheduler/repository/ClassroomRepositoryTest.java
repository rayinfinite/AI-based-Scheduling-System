package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.Classroom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClassroomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Test
    void testSaveAndFindById() {
        // Creating Test Data
        Classroom classroom = new Classroom();
        classroom.setName("Test Room");
        classroom.setSize(30);

        // Save to database
        Classroom savedClassroom = entityManager.persist(classroom);
        entityManager.flush();

        // Test Search
        Optional<Classroom> found = classroomRepository.findById(savedClassroom.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Room");
        assertThat(found.get().getSize()).isEqualTo(30);
    }

    @Test
    void testFindAll() {
        // Creating Test Data
        Classroom classroom1 = new Classroom();
        classroom1.setName("Room 1");
        Classroom classroom2 = new Classroom();
        classroom2.setName("Room 2");

        entityManager.persist(classroom1);
        entityManager.persist(classroom2);
        entityManager.flush();

        // Test to find all
        assertThat(classroomRepository.findAll()).hasSize(2);
    }

    @Test
    void testDelete() {
        // Creating Test Data
        Classroom classroom = new Classroom();
        classroom.setName("To Delete");
        
        Classroom savedClassroom = entityManager.persist(classroom);
        entityManager.flush();

        // removing
        classroomRepository.deleteById(savedClassroom.getId());

        // Verify Delete
        assertThat(classroomRepository.findById(savedClassroom.getId())).isEmpty();
    }
} 