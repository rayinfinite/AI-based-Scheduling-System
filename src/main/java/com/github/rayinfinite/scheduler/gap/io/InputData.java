package com.github.rayinfinite.scheduler.gap.io;

import com.github.rayinfinite.scheduler.entity.Classroom;
import com.github.rayinfinite.scheduler.gap.entity.*;
import com.github.rayinfinite.scheduler.gap.entity.Teacher1;
import com.github.rayinfinite.scheduler.gap.util.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yi
 */
public class InputData {

    public static void read() {
        // 手动初始化 TeachingPlan 数据
        List<TeachingPlan> planList = new ArrayList<>();
        TeachingPlan plan1 = new TeachingPlan();
        plan1.setCourseName("Course1");
        plan1.setCourseCode("CC1");
        plan1.setDuration(1);
        plan1.setSoftware("-");
        plan1.setCohort("Cohort1");
        plan1.setRun("1");
        plan1.setCourseDate(new Date());
        plan1.setWeek(1);
        plan1.setClassroom(1L);
        plan1.setTeacher1("TBC");
        plan1.setTeacher2(" ");
        plan1.setTeacher3(" ");
        plan1.setManager("-");
        plan1.setCert("-");
        plan1.setClassSize(30);
        planList.add(plan1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.PLAN_LIST = planList;

        // 手动初始化 Classroom 数据
        List<Classroom> classroomList = new ArrayList<>();
        Classroom classroom1 = new Classroom();
        classroom1.setId(1L);
        classroom1.setName("Room1");
        classroom1.setSize(30);
        classroom1.setSoftware("-");
        classroomList.add(classroom1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.CLASSROOM_LIST = classroomList;

        // 手动初始化 Teacher 数据
        List<Teacher1> teacher1List = new ArrayList<>();
        Teacher1 teacher1 = new Teacher1();
        teacher1.setId("T1");
        teacher1.setTeacher1("TBC");
        teacher1.setEnableCourse(List.of("course1"));
        teacher1.setUnavailableDate((List<Date>) new Date(1702108800000L));
        teacher1List.add(teacher1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TEACHER1_LIST = teacher1List;

        // 手动初始化 Teacher 数据
        List<Teacher2> teacher2List = new ArrayList<>();
        Teacher2 teacher2 = new Teacher2();
        teacher2.setId("T1");
        teacher2.setTeacher2("TBC");
        teacher2.setEnableCourse(List.of("course1"));
        teacher2.setUnavailableDate((List<Date>) new Date(1702108800000L));
        teacher2List.add(teacher2);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TEACHER2_LIST = teacher2List;// 手动初始化 Teacher 数据

        List<Teacher3> teacher3List = new ArrayList<>();
        Teacher3 teacher3 = new Teacher3();
        teacher3.setId("T1");
        teacher3.setTeacher3("TBC");
        teacher3.setEnableCourse(List.of("course1"));
        teacher3.setUnavailableDate((List<Date>) new Date(1702108800000L));
        teacher3List.add(teacher3);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TEACHER3_LIST = teacher3List;

        // 手动初始化 Time 数据
        List<Time> timeList = new ArrayList<>();
        Time time1 = new Time();
        time1.setDuration(1);
        time1.setWeekOrder(1);
        timeList.add(time1);

        // 将数据赋值给 Constant 类中的静态字段
        Constant.TIME_LIST = timeList;
    }
}
