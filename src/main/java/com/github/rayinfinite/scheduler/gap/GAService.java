package com.github.rayinfinite.scheduler.gap;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.entity.InputData;
import com.github.rayinfinite.scheduler.gap.concept.ABSSFitnessFunction;
import com.github.rayinfinite.scheduler.gap.concept.ABSSGeneMutationOperator;
import com.github.rayinfinite.scheduler.gap.concept.ABSSInitialConstraintChecker;
import com.github.rayinfinite.scheduler.gap.concept.ABSSSuperGene;
import com.github.rayinfinite.scheduler.gap.core.ABSSGene;
import com.github.rayinfinite.scheduler.gap.util.Constant;
import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.ThresholdSelector;
import org.jgap.impl.TwoWayMutationOperator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * ClassName: GAService
 * Package: com.github.rayinfinite.scheduler.gap
 * Description:
 *
 * @Author Li JiaLiang
 * @Create 2024/11/29 15:27
 */

@Service
public class GAService {
    public List<InputData> getSchedule(List<InputData> inputDataList, List<Classroom> classroomList) {
        return Collections.emptyList();
    }
    public boolean createCurriculum() {try {
//        //初始化数据
        com.github.rayinfinite.scheduler.gap.io.InputData.read();
        int geneSize = Constant.PLAN_LIST.size();
        int classroomSize = Constant.CLASSROOM_LIST.size();
        int timeSize = Constant.TIME_LIST.size();
        int teacher1Size = Constant.TEACHER1_LIST.size();
        int teacher2Size = Constant.TEACHER2_LIST.size();
        int teacher3Size = Constant.TEACHER3_LIST.size();

        //创建配置
        Configuration conf = new Configuration("1", "myconf");
        ABSSFitnessFunction fitnessFunction = new ABSSFitnessFunction(geneSize);
        ABSSInitialConstraintChecker timetableConstraintChecker = new ABSSInitialConstraintChecker();


        // 构建基因
        Gene[] testGenes = new Gene[geneSize];
        // 构建超级基因
        for (int i =0;i<geneSize;i++) {
            testGenes[i] = new ABSSSuperGene(conf,
                    new Gene[]{
                            new ABSSGene(conf, teacher1Size,Constant.TEACHER1),
                            new ABSSGene(conf, teacher2Size,Constant.TEACHER2),
                            new ABSSGene(conf, teacher3Size,Constant.TEACHER3),
                            new ABSSGene(conf, classroomSize,Constant.CLASSROOM),
                            new ABSSGene(conf, timeSize, Constant.TIME)
                    }
            );
        }

        System.out.println("===========================================================");
        //用上面的基因构建染色体
        Chromosome testChromosome = new Chromosome(conf, testGenes);
        //给染色体设置约束检查器
        testChromosome.setConstraintChecker(timetableConstraintChecker);
        //将染色体进行配置
        conf.setSampleChromosome(testChromosome);
        //配置种群的大小，也就是染色体的个数
        conf.setPopulationSize(Constant.POPULATION_SIZE);
        //配置适应度计算方法
        conf.setFitnessFunction(fitnessFunction);

        ThresholdSelector myBestChromosomesSelector = new ThresholdSelector(conf, 0.75);
        //配置自然选择器使用的是阈值选择器
        conf.addNaturalSelector(myBestChromosomesSelector, false);
        //配置随机生成器
        conf.setRandomGenerator(new StockRandomGenerator());
        conf.setEventManager(new EventManager());
        conf.setFitnessEvaluator(new DefaultFitnessEvaluator());

        CrossoverOperator myCrossoverOperator = new CrossoverOperator(conf);
        conf.addGeneticOperator(myCrossoverOperator);

        TwoWayMutationOperator myTwoWayMutationOperator = new TwoWayMutationOperator(conf);
        conf.addGeneticOperator(myTwoWayMutationOperator);

        ABSSGeneMutationOperator myMutationOperator = new ABSSGeneMutationOperator(conf);
        conf.addGeneticOperator(myMutationOperator);

        conf.setKeepPopulationSizeConstant(false);

        // Creating genotype
        Genotype population = Genotype.randomInitialGenotype(conf);
        System.out.println("Our Chromosome: \n "+testChromosome.getConfiguration().toString());

        System.out.println("========================evolution==========================");

        // 开始进化
        long start_t = System.currentTimeMillis();
        for (int i = 0; i< Constant.MAX_GENERATIONS;i++) {
            System.out.println(String.format("generation#: %-4d population size: %-2d   fitness: %.6f   penalty: %.6f",i, population.getPopulation().size(), population.getFittestChromosome().getFitnessValue(),1/population.getFittestChromosome().getFitnessValue()));

            if (population.getFittestChromosome().getFitnessValue() >= Constant.FITNESS_THRESHOLD) {
                break;
            }
            population.evolve();
        }
        long finish_t = System.currentTimeMillis();

        System.out.println("===================end of evolution========================");

        Chromosome fittestChromosome = (Chromosome) population.getFittestChromosome();
        System.out.println(String.format("===========The best chromosome---fitness=[%.2f]============",fittestChromosome.getFitnessValue()));

        System.out.println(String.format("Elapsed time: %ss",(finish_t - start_t) / 1000));
        //输出数据（（未写））
//        outputData.printToConsole(fittestChromosome);

        // reset configuration

        Configuration.reset("1");
//    timetable.clean()
//    } catch (InvalidConfigurationException e) {
        //排课失败（未写）
//        e.printStackTrace();
//        throw new JeecgBootException(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "排课失败");
    } catch (InvalidConfigurationException e) {
        e.printStackTrace();
    }
        return true;
    }
}
