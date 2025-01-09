package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    @Id
    @ExcelIgnore
    private int id;
    @ExcelProperty(converter = DateConverter.class)
    private Date date;
}
