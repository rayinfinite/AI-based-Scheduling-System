package com.github.rayinfinite.algorithm.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;

import java.math.BigDecimal;

public class IntegerConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) {
        BigDecimal number = context.getReadCellData().getNumberValue();
        if (number != null) {
            return number.intValue();
        }
        String value = context.getReadCellData().getStringValue();
        if (value == null || value.isBlank() || value.equals("-")) {
            return null;
        }
        return Integer.valueOf(value);
    }
}
