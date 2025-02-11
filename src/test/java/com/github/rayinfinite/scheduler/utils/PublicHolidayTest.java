package com.github.rayinfinite.scheduler.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PublicHolidayTest {

    @Test
    void testIsPublicHoliday() {
        // Test known Singapore public holidays (e.g. Lunar New Year)
        LocalDate newYear = LocalDate.of(2024, 1, 1);
        assertTrue(PublicHoliday.isPublicHoliday(newYear));
        
        // Test ordinary working days
        LocalDate normalDay = LocalDate.of(2024, 1, 2);
        assertFalse(PublicHoliday.isPublicHoliday(normalDay));
    }

    @Test
    void testDetails() {
        // Testing details of known public holidays
        LocalDate newYear = LocalDate.of(2024, 1, 1);
        String details = PublicHoliday.details(newYear);
        assertNotNull(details);
        assertTrue(details.contains("New Year"));

        // Testing of non-public holidays
        LocalDate normalDay = LocalDate.of(2024, 1, 2);
        assertNull(PublicHoliday.details(normalDay));
    }

    @Test
    void testInvalidYearRange() {
        // Years in which testing was out of range
        LocalDate pastDate = LocalDate.of(2017, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> 
            PublicHoliday.isPublicHoliday(pastDate)
        );

        LocalDate futureDate = LocalDate.of(LocalDate.now().getYear() + 2, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> 
            PublicHoliday.isPublicHoliday(futureDate)
        );
    }

    @Test
    void testMultipleYears() {
        // Testing queries across multiple years
        LocalDate date2024 = LocalDate.of(2024, 1, 1);
        LocalDate date2023 = LocalDate.of(2023, 1, 1);
        
        // Verification allows you to check holidays for different years
        assertTrue(PublicHoliday.isPublicHoliday(date2024));
        assertTrue(PublicHoliday.isPublicHoliday(date2023));
    }

    @Test
    void testConstructorPrivate() throws NoSuchMethodException {
        Constructor<PublicHoliday> constructor = PublicHoliday.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(IllegalStateException.class, exception.getCause());
        assertEquals("Utility class", exception.getCause().getMessage());
    }
} 