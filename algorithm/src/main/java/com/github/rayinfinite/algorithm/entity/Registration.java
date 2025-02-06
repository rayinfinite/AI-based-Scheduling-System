package com.github.rayinfinite.algorithm.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ColumnWidth(17)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class Registration {
    @ExcelProperty("Cohort")
    private String cohort;
    @ExcelProperty("Headcount")
    private int headcount;
    @ExcelProperty("Course Name")
    private String courseName;

}
