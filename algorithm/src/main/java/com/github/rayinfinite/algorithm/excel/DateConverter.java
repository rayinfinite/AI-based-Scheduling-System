package com.github.rayinfinite.algorithm.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter implements Converter<Date> {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");

    @Override
    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public Date convertToJavaData(ReadConverterContext<?> context) {
        try {
            switch (context.getReadCellData().getType()) {
                case NUMBER:
                    // Excel stores dates as numbers, where 1 is 1900-01-01
                    int days = context.getReadCellData().getNumberValue().intValue();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1899, Calendar.DECEMBER, 30);
                    calendar.add(Calendar.DAY_OF_MONTH, days);
                    return calendar.getTime();
                case STRING:
                    String value = context.getReadCellData().getStringValue();
                    if (value == null || value.isBlank()) {
                        return null;
                    }
                    return formatter.parse(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Date> context) {
        return new WriteCellData<>(formatter.format(context.getValue()));
    }
}