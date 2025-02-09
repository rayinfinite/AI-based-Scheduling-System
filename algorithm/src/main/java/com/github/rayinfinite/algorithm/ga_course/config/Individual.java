package com.github.rayinfinite.algorithm.ga_course.config;

import com.github.rayinfinite.algorithm.entity.Cohort;
import com.github.rayinfinite.algorithm.entity.Course;
import com.github.rayinfinite.algorithm.ga_course.Timetable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Getter
public class Individual {
    private final Random random = new Random();
    private int[] chromosome;
    @Setter
    private double fitness = -1;

    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
        for (int gene = 0; gene < chromosomeLength; gene++) {
            if (0.5 < random.nextDouble()) {
                this.setGene(gene, 1);
            } else {
                this.setGene(gene, 0);
            }
        }
    }

    public Individual(Timetable timetable) {
        int plansNum = timetable.getPlansNum(timetable);
        int chromosomeLength = plansNum * 5;

        for (Course course : timetable.getCourses().values()) {
//            int run = course.getRun();
            int duration = course.getDuration();
            int length1 = (duration - 1) * 5;
            chromosomeLength += length1 * 5;
//			chromosomeLength += (run - 1) * length1;
        }

        int newChromosome[] = new int[chromosomeLength];
        int chromosomeIndex = 0;

        for (Cohort cohort : timetable.getCohorts().values()) { // Newly initialised individuals are divided into full-time and part-time to optimise performance.
            String cohortType = cohort.getCohortType();
            for (int courseId : cohort.getCourseIds()) {
                Course course = timetable.getCourse(courseId);
                int timeslotId;
                if ("0".equals(cohortType)) {
                    timeslotId = timetable.getRandomWeekdayTimeslot().getId(); // working days
                } else if ("1".equals(cohortType)) {
                    timeslotId = timetable.getRandomFridaySaturdayTimeslot().getId(); // Friday or Saturday
                } else {
                    throw new IllegalArgumentException("Invalid cohortType: " + cohortType);
                }
                newChromosome[chromosomeIndex] = timeslotId;
                chromosomeIndex++;

                // Random assignment of classrooms
                int roomId = timetable.getRandomRoom().getId();
                newChromosome[chromosomeIndex] = roomId;
                chromosomeIndex++;

                // Getting information about professors
                int professorNum = course.getProfessorNum();
                int[] professorIds = course.getTeacherIds();

                professorNum = Math.min(professorNum, professorIds.length);

                List<Integer> professorList = new ArrayList<>();
                for (int id : professorIds) {
                    professorList.add(id);
                }

                // Disrupting the order of professors' lists
                Collections.shuffle(professorList);

                // Assigning professors to courses
                for (int i = 0; i < 3; i++) {
                    if (i < professorNum) {
                        newChromosome[chromosomeIndex] = professorList.get(i);
                    } else {
                        newChromosome[chromosomeIndex] = -1; // If there aren't enough professors, set it to -1
                    }
                    chromosomeIndex++;
                }
            }
            this.chromosome = newChromosome;
        }
    }

    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i : this.chromosome) {
            output.append(i);
        }
        return output.toString();
    }
}
