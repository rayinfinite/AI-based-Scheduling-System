package com.github.rayinfinite.scheduler.excel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IntegerConverterTest {

    @Test
    void testConvertToJavaData_number() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of(123));
        assertEquals(123, result);
    }

    @Test
    void testConvertToJavaData_string() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of("456"));
        assertEquals(456, result);
    }

    @Test
    void testConvertToJavaData_nullString() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of(null));
        assertNull(result);
    }

    @Test
    void testConvertToJavaData_blankString() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of(" "));
        assertNull(result);
    }

    @Test
    void testConvertToJavaData_dashString() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of("-"));
        assertNull(result);
    }

    @Test
    void testConvertToJavaData_invalidString() {
        IntegerConverter converter = new IntegerConverter();
        Integer result = converter.convertToJavaData(TestReadConverterContext.of("abc"));
        assertNull(result);
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