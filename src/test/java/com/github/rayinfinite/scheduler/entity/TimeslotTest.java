package com.github.rayinfinite.scheduler.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class TimeslotTest {
    
    @Test
    void testGetterAndSetter() {
        Timeslot timeslot = new Timeslot();
        Date date = new Date();
        
        timeslot.setId(1);
        timeslot.setDate(date);
        
        assertEquals(1, timeslot.getId());
        assertEquals(date, timeslot.getDate());
    }
    
    @Test
    void testAllArgsConstructor() {
        Date date = new Date();
        Timeslot timeslot = new Timeslot(1, date);
        
        assertEquals(1, timeslot.getId());
        assertEquals(date, timeslot.getDate());
    }
} 