package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CohortTest {
    
    @Test
    void testGetterAndSetter() {
        Cohort cohort = new Cohort();
        
        cohort.setId(1);
        cohort.setCohortSize(30);
        cohort.setCohortType("Type1");
        cohort.setName("TestCohort");
        cohort.setTypeId(2);
        cohort.setCourseIds(new int[]{1, 2, 3});
        
        assertEquals(1, cohort.getId());
        assertEquals(30, cohort.getCohortSize());
        assertEquals("Type1", cohort.getCohortType());
        assertEquals("TestCohort", cohort.getName());
        assertEquals(2, cohort.getTypeId());
        assertArrayEquals(new int[]{1, 2, 3}, cohort.getCourseIds());
    }
} 