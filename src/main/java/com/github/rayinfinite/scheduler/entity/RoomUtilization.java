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
@ColumnWidth(17)
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 11, bold = BooleanEnum.TRUE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 15)
public class RoomUtilization {
    @ExcelProperty("Room")
    private String room;
    @ExcelProperty("Used Days")
    private int usedDays;
    @ExcelProperty("Utilization Rate")
    private String utilizationRate;

}
