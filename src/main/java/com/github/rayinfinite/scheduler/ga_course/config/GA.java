package com.github.rayinfinite.scheduler.ga_course.config;

import com.github.rayinfinite.scheduler.ga_course.Timetable;

import java.security.SecureRandom;
import java.util.Random;

public class GA {
    private final int tournamentSize;
    private final int populationSize;
    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;
    private final Random random = new Random();

    public GA(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(Timetable timetable) {
        return new Population(this.populationSize, timetable);
    }

    public double calcFitness(Individual individual, Timetable timetable) {
        Timetable threadTimetable = new Timetable(timetable);
        threadTimetable.createPlans(individual);

        double penalty = threadTimetable.calcPenalty();

        double fitness = 1 / (penalty + 1);
        individual.setFitness(fitness);

        return fitness;
    }

    public void evalPopulation(Population population, Timetable timetable) {
        double populationFitness = 0;
        for (Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, timetable);
        }

        population.setPopulationFitness(populationFitness);
    }

    public boolean isTerminationConditionMet1(Population population) {
        return population.getFittest(0).getFitness() == 1.0;
    }

    public boolean isTerminationconditionMet2(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }

//	//均匀交叉
//	public Population crossoverPopulation(Population population) {
//		Population newPopulation = new Population(population.size());
//		for(int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
//			Individual parent1 = population.getFittest(populationIndex);
//			if(this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
//				Individual offspring = new Individual(parent1.getChromosomeLength());
//				Individual parent2 = selectParent((population));
//				for(int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
//					if(0.5 > Math.random()) {
//						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
//					} else {
//						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
//					}
//				}
//				newPopulation.setIndividual(populationIndex, offspring);
//			} else {
//				newPopulation.setIndividual(populationIndex, parent1);
//			}
//		}
//		return newPopulation;
//	}

    //锦标赛选父母
    public Individual selectParent(Population population) {
        Population tournament = new Population(this.tournamentSize);
        Random random = new SecureRandom();

        // 随机选择 tournamentSize 个个体放入锦标赛种群
        for (int i = 0; i < this.tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            Individual tournamentIndividual = population.getIndividual(randomIndex);
            tournament.setIndividual(i, tournamentIndividual);
        }

        return tournament.getFittest(0);
    }

    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population(population.size());
        int chromosomeLength = population.getIndividual(0).getChromosomeLength();

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            if (this.crossoverRate > random.nextDouble() && populationIndex >= this.elitismCount) {
                Individual offspring = new Individual(chromosomeLength);
                Individual parent2 = this.selectParent(population);

                // Generate random swap point
                int swapPoint = random.nextInt(chromosomeLength + 1);

                // Perform single-point crossover
                for (int geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                    if (geneIndex < swapPoint) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }

    public Population mutatePopulation(Population population, Timetable timetable) {
        Population newPopulation = new Population(this.populationSize);
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);

            Individual randomIndividual = new Individual(timetable);

            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                if (populationIndex > this.elitismCount && this.mutationRate > random.nextDouble()) {
                    individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                }
            }
            newPopulation.setIndividual(populationIndex, individual);
        }
        return newPopulation;
    }
}
