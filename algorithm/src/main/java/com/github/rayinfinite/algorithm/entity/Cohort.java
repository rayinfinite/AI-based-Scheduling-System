package com.github.rayinfinite.algorithm.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cohort {
    @Id
    @ExcelIgnore
    private int id;
    private int cohortSize;
    private String cohortType;
    private String name;

    @ExcelIgnore
    private int typeId;
    @ExcelIgnore
    private int[] courseIds;
}
