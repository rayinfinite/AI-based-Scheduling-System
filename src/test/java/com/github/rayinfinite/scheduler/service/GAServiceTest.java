package com.github.rayinfinite.scheduler.service;

import com.github.rayinfinite.scheduler.entity.Cohort;
import com.github.rayinfinite.scheduler.entity.Course;
import com.github.rayinfinite.scheduler.entity.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GAServiceTest {

    @InjectMocks
    private GAService gaService;

    private Method createMapMethod;
    private Method setCourseCohortIdMethod;
    private Method getProfessorMapMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Getting Private Methods
        createMapMethod = GAService.class.getDeclaredMethod("createMap", List.class, Function.class);
        createMapMethod.setAccessible(true);

        setCourseCohortIdMethod = GAService.class.getDeclaredMethod("setCourseCohortId", List.class, Map.class);
        setCourseCohortIdMethod.setAccessible(true);

        getProfessorMapMethod = GAService.class.getDeclaredMethod("getProfessorMap", List.class);
        getProfessorMapMethod.setAccessible(true);
    }

    @Test
    void testCreateMap() throws Exception {
        // Preparing Test Data
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1);
        Course course2 = new Course();
        course2.setId(2);
        courses.add(course1);
        courses.add(course2);

        // Creating a Function Object
        Function<Course, Integer> idFunction = Course::getId;

        // execute a test
        @SuppressWarnings("unchecked")
        Map<Integer, Course> result = (Map<Integer, Course>) createMapMethod.invoke(gaService, courses, idFunction);

        // Verification results
        assertThat(result).hasSize(2);
        assertThat(result).containsEntry(1, course1).containsEntry(2, course2);
    }

    @Test
    void testSetCourseCohortId() throws Exception {
        // Preparing Test Data
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setCohort("Cohort1");
        courses.add(course);

        Map<Integer, Cohort> cohortMap = Map.of(1, createCohort(1, "Cohort1"));

        // execute a test
        setCourseCohortIdMethod.invoke(gaService, courses, cohortMap);

        // Verification results
        assertThat(course.getCohortId()).isEqualTo(1);
    }

    @Test
    void testGetProfessorMap() throws Exception {
        // Preparing Test Data
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setTeacher1("Teacher1");
        course1.setTeacher2("Teacher2");
        Course course2 = new Course();
        course2.setTeacher1("Teacher1");
        course2.setTeacher3("Teacher3");
        courses.add(course1);
        courses.add(course2);

        // execute a test
        @SuppressWarnings("unchecked")
        Map<Integer, Professor> result = (Map<Integer, Professor>) getProfessorMapMethod.invoke(gaService, courses);

        // Verification results
        assertThat(result).hasSize(3);
        assertThat(result.values())
            .extracting(Professor::getProfessorName)
            .containsExactlyInAnyOrder("Teacher1", "Teacher2", "Teacher3");
        
        assertThat(course1.getTeacherIds()).hasSize(2);
        assertThat(course2.getTeacherIds()).hasSize(2);
        assertThat(course1.getProfessorNum()).isEqualTo(2);
        assertThat(course2.getProfessorNum()).isEqualTo(2);
    }

    @Test
    void testSetCourseCohortIdWithNonExistentCohort() throws Exception {
        // Preparing Test Data
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setCohort("NonExistentCohort");
        courses.add(course);

        Map<Integer, Cohort> cohortMap = Map.of(1, createCohort(1, "Cohort1"));

        // execute a test
        setCourseCohortIdMethod.invoke(gaService, courses, cohortMap);

        // Verification results
        assertThat(course.getCohortId()).isEqualTo(-1);
    }

    @Test
    void testGetProfessorMapWithEmptyTeachers() throws Exception {
        // Preparing Test Data
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setTeacher1("");
        course.setTeacher2(null);
        course.setTeacher3("");
        courses.add(course);

        // execute a test
        @SuppressWarnings("unchecked")
        Map<Integer, Professor> result = (Map<Integer, Professor>) getProfessorMapMethod.invoke(gaService, courses);

        // Verification results
        assertThat(result).isEmpty();
        assertThat(course.getProfessorNum()).isZero();
        assertThat(course.getTeacherIds()).isEmpty();
    }

    private Cohort createCohort(int id, String name) {
        Cohort cohort = new Cohort();
        cohort.setId(id);
        cohort.setName(name);
        return cohort;
    }
} 