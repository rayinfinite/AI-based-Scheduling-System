package com.github.rayinfinite.scheduler.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.rayinfinite.scheduler.excel.DateConverter;
import com.github.rayinfinite.scheduler.excel.IntegerConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
public class Course {
    @Id
    @ExcelIgnore
    Integer id;
    String practiceArea;
    String courseName;
    String courseCode;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer duration;
    String software;
    //    @ExcelIgnore
    @ExcelProperty(converter = IntegerConverter.class)
    Integer cohortId;
    String cohort;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer run;
    @ExcelIgnore
    @ExcelProperty(converter = DateConverter.class)
    Date courseDate;

    @ExcelIgnore
    String week;
    @ExcelIgnore
    String classroom;

    String teacher1;
    String teacher2;
    String teacher3;

    String manager;
    String cert;
    @ExcelProperty(converter = IntegerConverter.class)
    Integer professorNum;

    @ExcelIgnore
    @Transient
    int[] teacherIds;
}
