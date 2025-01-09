package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassroomTest {
    
    @Test
    void testGetterAndSetter() {
        Classroom classroom = new Classroom();
        
        classroom.setId(1);
        classroom.setName("Room101");
        classroom.setSize(30);
        classroom.setSoftware("Visual Studio");
        
        assertEquals(1, classroom.getId());
        assertEquals("Room101", classroom.getName());
        assertEquals(30, classroom.getSize());
        assertEquals("Visual Studio", classroom.getSoftware());
    }
    
    @Test
    void testConstructorWithParameters() {
        Classroom classroom = new Classroom(1, "Room101", 30);
        
        assertEquals(1, classroom.getId());
        assertEquals("Room101", classroom.getName());
        assertEquals(30, classroom.getSize());
    }
} 