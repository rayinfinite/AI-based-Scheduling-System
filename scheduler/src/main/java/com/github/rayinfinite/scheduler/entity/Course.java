package com.github.rayinfinite.scheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    Integer id;
    String practiceArea;
    String courseName;
    String courseCode;
    Integer duration;
    String software;

    Integer cohortId;
    String cohort;
    Integer run;
    Date courseDate;
    String week;
    String classroom;

    String teacher1;
    String teacher2;
    String teacher3;
    String manager;
    String cert;

    Integer professorNum;
}
