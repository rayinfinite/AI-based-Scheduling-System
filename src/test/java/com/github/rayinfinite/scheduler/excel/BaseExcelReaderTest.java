package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseExcelReaderTest {

    @Test
    void testReadExcel() throws IOException {
        // 获取测试 Excel 文件资源
        URL resource = getClass().getClassLoader().getResource("inputdata1.xlsx"); // inputdata1.xlsx 放在 test/resources 目录下
        assertNotNull(resource);
        File excelFile = new File(resource.getFile());

        // 创建读取器
        BaseExcelReader<TestData> reader = new BaseExcelReader<>();

        // 读取 Excel 文件
        EasyExcel.read(excelFile, TestData.class, reader).sheet().doRead();

        // 验证读取结果
        List<TestData> dataList = reader.getDataList();
        assertNotNull(dataList);
        assertEquals(3, dataList.size());
        assertEquals("Name1", dataList.get(0).getName());
        assertEquals(10, dataList.get(0).getValue());
        assertEquals("2024/1/1",dataList.get(0).getDate());
        assertEquals("Name2", dataList.get(1).getName());
        assertEquals(20, dataList.get(1).getValue());
        assertEquals("Name3", dataList.get(2).getName());
        assertEquals(30, dataList.get(2).getValue());
    }

    // 用于测试的数据类
    @lombok.Data
    public static class TestData {
        private String name;
        private Integer value;
        private String date;
    }
}
