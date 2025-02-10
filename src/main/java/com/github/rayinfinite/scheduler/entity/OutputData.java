package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import lombok.Data;

import java.util.Date;

@Data
@ColumnWidth(16)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class OutputData {
    @ColumnWidth(17)
    @ExcelProperty("Practice Area (PA)")
    String practiceArea;

    @ExcelProperty("Course Name")
    String courseName;

    @ColumnWidth(12)
    @ExcelProperty("Course Code")
    String courseCode;

    @ExcelProperty("Duration (Days)")
    Integer duration;

    @ColumnWidth(9)
    @ExcelProperty("Software")
    String software;

    @ColumnWidth(8)
    @ExcelProperty("Cohort")
    String cohort;

    @ColumnWidth(8)
    @ExcelProperty("Run #")
    Integer run;

    @ColumnWidth(12)
    @ExcelProperty(value = "Course Date", converter = DateConverter.class)
    Date courseDate;

    @ColumnWidth(11)
    @ExcelProperty("Course Day")
    String week;

    @ExcelProperty("Classroom")
    String classroom;

    @ExcelProperty("Teaching Staff (1)")
    String teacher1;

    @ExcelProperty("Teaching Staff (2)")
    String teacher2;

    @ExcelProperty("Teaching Staff (3)")
    String teacher3;

    @ExcelProperty("Course Manager")
    String manager;

    @ColumnWidth(17)
    @ExcelProperty("Grad Cert (MTech)")
    String cert;
}
