package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    
    @Test
    void testGetterAndSetter() {
        Course course = new Course();
        Date date = new Date();
        
        course.setId(1);
        course.setPracticeArea("Area1");
        course.setCourseName("Java");
        course.setCourseCode("CS101");
        course.setDuration(120);
        course.setSoftware("Eclipse");
        course.setCohortId(1);
        course.setCohort("Cohort1");
        course.setRun(1);
        course.setCourseDate(date);
        course.setWeek("Monday");
        course.setClassroom("Room101");
        course.setTeacher1("Teacher1");
        course.setTeacher2("Teacher2");
        course.setTeacher3("Teacher3");
        course.setManager("Manager1");
        course.setCert("Cert1");
        course.setProfessorNum(3);
        course.setTeacherIds(new int[]{1, 2, 3});
        
        assertEquals(1, course.getId());
        assertEquals("Area1", course.getPracticeArea());
        assertEquals("Java", course.getCourseName());
        assertEquals("CS101", course.getCourseCode());
        assertEquals(120, course.getDuration());
        assertEquals("Eclipse", course.getSoftware());
        assertEquals(1, course.getCohortId());
        assertEquals("Cohort1", course.getCohort());
        assertEquals(1, course.getRun());
        assertEquals(date, course.getCourseDate());
        assertEquals("Monday", course.getWeek());
        assertEquals("Room101", course.getClassroom());
        assertEquals("Teacher1", course.getTeacher1());
        assertEquals("Teacher2", course.getTeacher2());
        assertEquals("Teacher3", course.getTeacher3());
        assertEquals("Manager1", course.getManager());
        assertEquals("Cert1", course.getCert());
        assertEquals(3, course.getProfessorNum());
        assertArrayEquals(new int[]{1, 2, 3}, course.getTeacherIds());
    }
} 