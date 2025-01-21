package com.github.rayinfinite.scheduler.excel;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateConverterTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    @Test
    void testConvertToJavaData_number_2024() {
        DateConverter converter = new DateConverter();

        // 2024-01-01 对应的 Excel 数字是 44927
        Date date = converter.convertToJavaData(TestReadConverterContext.of(44927));

        LocalDate localDate = LocalDate.of(2024, 1, 1);
        Date expectedDate = Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());

        assertEquals(formatter.format(expectedDate), formatter.format(date));
    }

    @Test
    void testConvertToJavaData_string_2024() {
        DateConverter converter = new DateConverter();
        Date date = converter.convertToJavaData(TestReadConverterContext.of("2024/10/26"));
        LocalDate expectedDate = LocalDate.parse("2024/10/26", formatter);
        assertEquals(Date.from(expectedDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant()), date);
    }

    @Test
    void testConvertToJavaData_nullString() {
        DateConverter converter = new DateConverter();
        Date date = converter.convertToJavaData(TestReadConverterContext.of(null));
        assertNull(date);
    }

    @Test
    void testConvertToJavaData_blankString() {
        DateConverter converter = new DateConverter();
        Date date = converter.convertToJavaData(TestReadConverterContext.of(" "));
        assertNull(date);
    }

    @Test
    void testConvertToJavaData_wrongFormat() {
        DateConverter converter = new DateConverter();
        Date date = converter.convertToJavaData(TestReadConverterContext.of("2024-10-26")); // 错误的格式
        assertNull(date);
    }

    // 辅助类，简化测试 (与之前相同)
    private static class TestReadConverterContext {
        // ... (与之前相同)
        private final Object value;

        private TestReadConverterContext(Object value) {
            this.value = value;
        }

        public static com.alibaba.excel.converters.ReadConverterContext<?> of(Object value) {
            return new com.alibaba.excel.converters.ReadConverterContext<Object>() {
                @Override
                public com.alibaba.excel.metadata.data.ReadCellData<?> getReadCellData() {
                    return new com.alibaba.excel.metadata.data.ReadCellData<Object>() {
                        @Override
                        public Object getValue() {
                            return value;
                        }

                        @Override
                        public com.alibaba.excel.metadata.data.CellData.Type getType() {
                            if (value == null) {
                                return com.alibaba.excel.metadata.data.CellData.Type.EMPTY;
                            } else if (value instanceof Number) {
                                return com.alibaba.excel.metadata.data.CellData.Type.NUMBER;
                            } else {
                                return com.alibaba.excel.metadata.data.CellData.Type.STRING;
                            }
                        }

                        @Override
                        public java.math.BigDecimal getNumberValue() {
                            return value instanceof Number ? new java.math.BigDecimal(value.toString()) : null;
                        }

                        @Override
                        public String getStringValue() {
                            return value != null ? value.toString() : null;
                        }
                    };
                }
            };
        }
    }
}