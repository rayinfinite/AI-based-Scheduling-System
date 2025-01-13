package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateConverterTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");

    @Test
    void testConvertToJavaData_number() {
        DateConverter converter = new DateConverter();
        ReadConverterContext<Date> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.NUMBER);
        Mockito.when(cellData.getNumberValue()).thenReturn(new java.math.BigDecimal(43831)); // Example: 2020-01-01
        Date date = converter.convertToJavaData(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, Calendar.JANUARY, 1);
        Date expectedDate = calendar.getTime();

        assertEquals(formatter.format(expectedDate), formatter.format(date));
    }

    @Test
    void testConvertToJavaData_string() throws java.text.ParseException {
        DateConverter converter = new DateConverter();
        ReadConverterContext<Date> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.STRING);
        Mockito.when(cellData.getStringValue()).thenReturn("2024/1/1");
        Date date = converter.convertToJavaData(context);
        Date expectedDate = formatter.parse("2024/1/1");
        assertEquals(expectedDate, date);
    }

    @Test
    void testConvertToJavaData_nullString() {
        DateConverter converter = new DateConverter();
        ReadConverterContext<Date> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.STRING);
        Mockito.when(cellData.getStringValue()).thenReturn(null);
        Date date = converter.convertToJavaData(context);
        assertNull(date);
    }

    @Test
    void testConvertToJavaData_blankString() {
        DateConverter converter = new DateConverter();
        ReadConverterContext<Date> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.STRING);
        Mockito.when(cellData.getStringValue()).thenReturn(" ");
        Date date = converter.convertToJavaData(context);
        assertNull(date);
    }
}
