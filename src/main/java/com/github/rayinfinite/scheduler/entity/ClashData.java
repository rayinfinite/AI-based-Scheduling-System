package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ColumnWidth(22)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class ClashData {
    @ExcelProperty("Clash Type")
    private String clashType;
    @ExcelProperty("Clash Num")
    private Integer clashCount;
    @ExcelProperty("Room")
    private String roomName;
    @ExcelProperty("Date")
    private String date;

}
