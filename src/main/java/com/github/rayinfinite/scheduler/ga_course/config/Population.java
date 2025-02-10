package com.github.rayinfinite.scheduler.ga_course.config;

import com.github.rayinfinite.scheduler.ga_course.Timetable;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class Population {
    private final Individual[] population;
    @Setter
    @Getter
    private double populationFitness = -1;

    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }

    public Population(int populationSize, Timetable timetable) {
        this.population = new Individual[populationSize];

        for (int individualCount = 0; individualCount < populationSize; individualCount++) {
            Individual individual = new Individual(timetable);
            this.population[individualCount] = individual;
        }
    }

    public Individual[] getIndividuals() {
        return this.population;
    }

    public Individual getFittest(int offset) {
        Arrays.sort(this.population, (o1, o2) -> {
            if (o1.getFitness() > o2.getFitness()) {
                return -1;
            } else if (o1.getFitness() < o2.getFitness()) {
                return 1;
            }
            return 0;
        });
        return this.population[offset];
    }

    public int size() {
        return this.population.length;
    }

    public void setIndividual(int offset, Individual individual) {
        population[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return population[offset];
    }

}