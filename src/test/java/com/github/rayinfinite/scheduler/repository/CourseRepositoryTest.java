package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testSaveAndFindById() {
        Course course = new Course();
        course.setId(1);
        course.setCourseName("Test Course");
        course.setCourseCode("TC101");
        course.setDuration(120);

        Course savedCourse = entityManager.persist(course);
        entityManager.flush();

        Optional<Course> found = courseRepository.findById(savedCourse.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCourseName()).isEqualTo("Test Course");
        assertThat(found.get().getCourseCode()).isEqualTo("TC101");
    }

    @Test
    void testFindByCourseDateBetween() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setCourseDate(new Date(124, 2, 20)); // 2024-03-20

        Course course2 = new Course();
        course2.setId(2);
        course2.setCourseDate(new Date(124, 2, 21)); // 2024-03-21

        Course course3 = new Course();
        course3.setId(3);
        course3.setCourseDate(new Date(124, 2, 22)); // 2024-03-22

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        Date startDate = new Date(124, 2, 20);
        Date endDate = new Date(124, 2, 21);
        List<Course> courses = courseRepository.findByCourseDateBetween(startDate, endDate);

        assertThat(courses).hasSize(2);
    }

    @Test
    void testFindTeachers() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setTeacher1("Teacher A");
        course1.setTeacher2("Teacher B");
        course1.setTeacher3("Teacher C");

        Course course2 = new Course();
        course2.setId(2);
        course2.setTeacher1("Teacher A");
        course2.setTeacher2("Teacher D");
        course2.setTeacher3("Teacher E");

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.flush();

        List<String> teacher1List = courseRepository.findTeacher1();
        List<String> teacher2List = courseRepository.findTeacher2();
        List<String> teacher3List = courseRepository.findTeacher3();

        assertThat(teacher1List).hasSize(1).contains("Teacher A");
        assertThat(teacher2List).hasSize(2).contains("Teacher B", "Teacher D");
        assertThat(teacher3List).hasSize(2).contains("Teacher C", "Teacher E");
    }

    @Test
    void testDelete() {
        Course course = new Course();
        course.setId(1);
        course.setCourseName("To Delete");
        
        Course savedCourse = entityManager.persist(course);
        entityManager.flush();

        courseRepository.deleteById(savedCourse.getId());

        assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
    }
} 