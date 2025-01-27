package com.github.rayinfinite.algorithm.ga_course;

import lombok.Getter;
import lombok.Setter;

@Getter
public class TeachingPlan {
    private final int planId;
    private final int cohortId;
    private final int courseId;
    private int professor1Id;
    private int professor2Id;
    private int professor3Id;
    private int timeslotId;
    @Setter
    private int roomId;

    public TeachingPlan(int planId, int cohortId, int courseId) {
        this.planId = planId;
        this.courseId = courseId;
        this.cohortId = cohortId;
    }

    public void addProfessor1(int professorId) {
        this.professor1Id = professorId;
    }

    public void addProfessor2(int professorId) {
        this.professor2Id = professorId;
    }

    public void addProfessor3(int professorId) {
        this.professor3Id = professorId;
    }

    public void addTimeslot(int timeslotId) {
        this.timeslotId = timeslotId;
    }
}
