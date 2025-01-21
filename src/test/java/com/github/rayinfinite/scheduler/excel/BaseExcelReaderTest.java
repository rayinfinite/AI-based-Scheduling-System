package com.github.rayinfinite.scheduler.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityExcelIntegrationTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void testReadExcelAndMapToEntity() throws IOException, ParseException {
        // 1. 获取测试 Excel 文件资源 (使用 inputdata1.xlsx)
        URL resource = getClass().getClassLoader().getResource("inputdata1.xlsx");
        assertNotNull(resource);
        File excelFile = new File(resource.getFile());

        // 2. 创建读取器
        BaseExcelReader<InputData> reader = new BaseExcelReader<>();

        // 3. 读取 Excel 文件，并指定转换器 (在 InputData 类中)
        EasyExcel.read(excelFile, InputData.class, reader).sheet().doRead();

        // 4. 验证读取结果
        List<InputData> dataList = reader.getDataList();
        assertNotNull(dataList);
        assertEquals(1, dataList.size()); // 假设 inputdata1.xlsx 只有 1 行数据

        InputData data = dataList.get(0);

        assertEquals("PA2", data.getPracticeArea());
        assertEquals("Course1", data.getCourseName());
        assertEquals("CC2", data.getCourseCode());
        assertEquals(1, data.getDuration());
        assertEquals(1, data.getCohortId());
        assertEquals("Cohort1", data.getCohort());
        assertEquals(1, data.getRun());
        assertEquals("Lecturer9", data.getTeaching1());
        assertEquals("Lecturer1", data.getManager());
        assertEquals("Grad Cert 1", data.getCert());
        assertEquals(1, data.getProfessorNum());
        assertEquals(30, data.getCohortSize());
        assertEquals(1, data.getCohortType());
        assertEquals("Cohort1", data.getCohort2());
        assertEquals(formatter.parse("2024-02-07"), data.getCourseDate());


    }

    @Data
    public static class InputData {
        @ExcelProperty("Practice Area")
        private String practiceArea;
        @ExcelProperty("Course Name")
        private String courseName;
        @ExcelProperty("Course Code")
        private String courseCode;
        @ExcelProperty(value = "Duration", converter = IntegerConverter.class)
        private Integer duration;
        @ExcelProperty("Cohort ID")
        private Integer cohortId;
        @ExcelProperty(value = "Cohort", index = 5)
        private String cohort;
        @ExcelProperty(value = "Run", converter = IntegerConverter.class)
        private Integer run;
        @ExcelProperty("Teaching 1")
        private String teaching1;
        @ExcelProperty("Manager")
        private String manager;
        @ExcelProperty("Cert")
        private String cert;
        @ExcelProperty(value = "Professor Num", converter = IntegerConverter.class)
        private Integer professorNum;
        @ExcelProperty(value = "Cohort Size", converter = IntegerConverter.class)
        private Integer cohortSize;
        @ExcelProperty(value = "Cohort Type", converter = IntegerConverter.class)
        private Integer cohortType;
        @ExcelProperty(value = "Cohort", index = 13)
        private String cohort2;
        @ExcelProperty(value = "timeslot", converter = DateConverter.class)
        private Date courseDate;
    }

    @lombok.Data
    public static class TestData {
        private String name;
        private Integer value;
        private String date;
    }
}




