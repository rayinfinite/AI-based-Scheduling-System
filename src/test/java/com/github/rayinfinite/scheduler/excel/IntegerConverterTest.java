package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IntegerConverterTest {

    @Test
    void testConvertToJavaData_number() {
        IntegerConverter converter = new IntegerConverter();
        ReadConverterContext<Integer> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.NUMBER);
        Mockito.when(cellData.getNumberValue()).thenReturn(new BigDecimal("123"));
        Integer result = converter.convertToJavaData(context);
        assertEquals(123, result);
    }

    @Test
    void testConvertToJavaData_string() {
        IntegerConverter converter = new IntegerConverter();
        ReadConverterContext<Integer> context = Mockito.mock(ReadConverterContext.class);
        CellData cellData = Mockito.mock(CellData.class);
        Mockito.when(context.getReadCellData()).thenReturn(cellData);
        Mockito.when(cellData.getType()).thenReturn(CellData.Type.STRING);
    }
}