package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {
    
    @Test
    void testConstructorAndGetter() {
        Professor professor = new Professor(1, "John Doe");
        
        assertEquals(1, professor.getProfessorId());
        assertEquals("John Doe", professor.getProfessorName());
    }
} 