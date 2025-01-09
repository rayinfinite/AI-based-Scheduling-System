package com.github.rayinfinite.scheduler.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PublicHolidayTest {

    @Test
    void testIsPublicHoliday() {
        // 测试已知的新加坡公共假期（如农历新年）
        LocalDate newYear = LocalDate.of(2024, 1, 1);
        assertTrue(PublicHoliday.isPublicHoliday(newYear));
        
        // 测试普通工作日
        LocalDate normalDay = LocalDate.of(2024, 1, 2);
        assertFalse(PublicHoliday.isPublicHoliday(normalDay));
    }

    @Test
    void testDetails() {
        // 测试已知的公共假期的详细信息
        LocalDate newYear = LocalDate.of(2024, 1, 1);
        String details = PublicHoliday.details(newYear);
        assertNotNull(details);
        assertTrue(details.contains("New Year"));

        // 测试非公共假期
        LocalDate normalDay = LocalDate.of(2024, 1, 2);
        assertNull(PublicHoliday.details(normalDay));
    }

    @Test
    void testInvalidYearRange() {
        // 测试超出范围的年份
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
        // 测试跨多个年份的查询
        LocalDate date2024 = LocalDate.of(2024, 1, 1);
        LocalDate date2023 = LocalDate.of(2023, 1, 1);
        
        // 验证可以查询不同年份的假期
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