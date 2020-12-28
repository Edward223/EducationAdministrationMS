package com.EAMS.service.impl;

import com.EAMS.dao.impl.*;
import com.EAMS.domain.*;
import com.EAMS.exception.ArrangeCourseException;
import com.EAMS.service.TimetableService;
import com.EAMS.util.DateTimeUtil;
import com.EAMS.util.JdbcUtil;
import com.EAMS.util.UUIDUtil;
import com.EAMS.vo.TimetableVo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TimetableServiceImpl implements TimetableService {
    Connection connection = JdbcUtil.getCon();
    TimetableDaoImpl timetableDao = new TimetableDaoImpl(connection);
    TimeslotDaoImpl timeslotDao = new TimeslotDaoImpl(connection);
    CourseDaoImpl courseDao = new CourseDaoImpl(connection);
    ClassesDaoImpl classesDao = new ClassesDaoImpl(connection);
    InstituteDaoImpl instituteDao = new InstituteDaoImpl(connection);
    MajorDaoImpl majorDao = new MajorDaoImpl(connection);
    TeacherDaoImpl teacherDao = new TeacherDaoImpl(connection);
    ClassroomDaoImpl classroomDao = new ClassroomDaoImpl(connection);

    @Override
    public List<TimetableVo> getTimetableVoList(Map<String, String> map) {
        List<TimetableVo> timetableList = timetableDao.getTimetableVoList(map);
        for(TimetableVo t : timetableList){
            String[] timeslots = t.getTimeslot().split(",");
            Timeslot begin = timeslotDao.getTimeslotById(timeslots[0]);
            Timeslot end = null;
            if(timeslots.length > 1){
                end = timeslotDao.getTimeslotById(timeslots[timeslots.length-1]);
            }
            String timeslot = DateTimeUtil.concatTimeslot(begin,end);
            t.setTimeslot(timeslot);
        }
        return timetableList;
    }

    @Override
    public boolean deleteById(String id) {
        boolean flag = true;
        try {
            timetableDao.getConnection().setAutoCommit(false);
            int result = timetableDao.deleteById(id);
            if(result != 1){
                flag = false;
                timetableDao.getConnection().rollback();
            }
            timetableDao.getConnection().commit();
            timetableDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean init() {
        boolean flag = true;
        try {
            timetableDao.getConnection().setAutoCommit(false);
            int num = timetableDao.getTimetableNum();
            int result = timetableDao.init();
            if(result != num){
                flag = false;
                timetableDao.getConnection().rollback();
            }
            timetableDao.getConnection().commit();
            timetableDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public Timeslot getTimeslotByCondition(String day, String time) {
        return timeslotDao.getTimeslotByCondition(day,time);
    }

    @Override
    public boolean saveTimetable(Timetable timetable) throws ArrangeCourseException {
        boolean flag = true;
        Course course = courseDao.getCourseById(timetable.getCourseId());
        Classes clazz = classesDao.getClassById(timetable.getClassId());
        Classroom classroom = classroomDao.getClassroomById(timetable.getClassroomId());

        //判断是否已经上了这门课
        if(timetableDao.getTimetableByClassAndCourse(clazz.getId(),course.getId()) != 0){
            throw new ArrangeCourseException("该班级已经上过该课程");
        }

        //判断教室是否足够容纳班级人数
        if(Integer.parseInt(classroom.getHoldNum()) < Integer.parseInt(clazz.getStudentNum())){
            throw new ArrangeCourseException("选择的教室过小");
        }

        //判断20周内能否上完课程
        String[] period = course.getPeriod().split("x");
        int beginWeek = Integer.parseInt(timetable.getWeeks());
        int endWeek = beginWeek + Integer.parseInt(period[0]);
        if(endWeek > 21){
            throw new ArrangeCourseException("无法按时完成课时任务,一共需要上"+period[0]+"周课");
        }

        //判断当天能否一次上完一天的课程
        int beginTimeslot = Integer.parseInt(timetable.getTimeslotId());
        int endTimeslot = beginTimeslot + Integer.parseInt(period[1]);
        if(beginTimeslot%10 + Integer.parseInt(period[1]) > 10){
            throw new ArrangeCourseException("无法按时完成课时任务，每天需要上"+period[1]+"节课");
        }

        String[] weeks1 = new String[endWeek+beginWeek+1];
        for (int i = beginWeek; i <= endWeek; i++) {
            weeks1[i] = String.valueOf(i);
        }
        Set<String> WeekSet1 =  new HashSet<>(Arrays.asList(weeks1));//当前添加课程所需要上的周次

        //判断上课时间内教室是否有被占用
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week1 = timetableDao.getWeeksByRoomAndTimeslot(timetable.getClassroomId(),String.valueOf(i));
            String[] weeks2 = week1.split(",");
            Set<String> weekSet2 =  new HashSet<>(Arrays.asList(weeks2));//课程表该时间段内教室被占用的周次
            int before1 = weekSet2.size();
            weekSet2.removeAll(WeekSet1);
            if(before1 != weekSet2.size()){
                throw new ArrangeCourseException("教室被占用");
            }
        }

        //判断上课时间内教师是否已经有课
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week2 = timetableDao.getWeeksByTeacherAndTimeslot(timetable.getTeacherId(),String.valueOf(i));
            String[] weeks3 = week2.split(",");
            Set<String> weekSet3 =  new HashSet<>(Arrays.asList(weeks3));//课程表该时间段内教室被占用的周次
            int before2 = weekSet3.size();
            weekSet3.removeAll(WeekSet1);
            if(before2 != weekSet3.size()){
                throw new ArrangeCourseException("该教师在当前时间内已经有课");
            }
        }

        //判断上课时间内班级是否已经有课
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week3 = timetableDao.getWeeksByClassesAndTimeslot(timetable.getClassId(),String.valueOf(i));
            String[] weeks4 = week3.split(",");
            Set<String> weekSet4 =  new HashSet<>(Arrays.asList(weeks4));//课程表该时间段内教室被占用的周次
            int before3 = weekSet4.size();
            weekSet4.removeAll(WeekSet1);
            if(before3 != weekSet4.size()){
                throw new ArrangeCourseException("该班级在当前时间内已经有课");
            }
        }

        try {
            timetableDao.getConnection().setAutoCommit(false);
            StringBuilder timeslot = new StringBuilder("");
            for (int i = beginTimeslot; i < endTimeslot; i++) {
                timeslot.append(i);
                if(i!=endTimeslot-1){
                    timeslot.append(",");
                }
            }
            timetable.setTimeslotId(timeslot.toString());
            StringBuilder week = new StringBuilder("");
            for (int i = beginWeek; i < endWeek; i++) {
                week.append("("+i+")");
                if(i!=endWeek-1){
                    week.append(",");
                }
            }
            timetable.setWeeks(week.toString());

            int result = timetableDao.save(timetable);
            if(result != 1){
                flag = false;
                timetableDao.getConnection().rollback();
            }
            timetableDao.getConnection().commit();
            timetableDao.getConnection().setAutoCommit(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public void autoArrangeCourse() {
        Timetable timetable = new Timetable();

        //获得所有学院
        List<Institute> instituteList = instituteDao.getInstituteList();
        for (Institute institute : instituteList) {
            //获得所有专业
            List<Major> majorList = majorDao.getMajorListByIid(institute.getId());
            for (Major major : majorList) {
                String[] grades = DateTimeUtil.getInSchoolGrade();
                String semester = DateTimeUtil.getSemester();
                for (int i = 1; i <= 4; i++) {
                    //获得当前专业在当前年级学期时需要上课的课程
                    List<Course> courseList = courseDao.getCoursePlanListByClass(major.getName(),String.valueOf(i),semester);
                    for (Course course : courseList) {
                        timetable.setCourseId(course.getId());

                        //获得当前年级里该专业的所有班级
                        List<Classes> classesList = classesDao.getClassListByCondition(major.getName(),grades[i]);
                        for (Classes classes : classesList) {
                            timetable.setClassId(classes.getId());

                            //在能教这门课的老师中找到当前工作量最少的一个老师
                            Teacher teacher = teacherDao.getTeacherWithMinWorkloadByCid(course.getId());
                            timetable.setTeacherId(teacher.getId());

                            //获得课程计划中课程所需课时
                            String[] period = course.getPeriod().split("x");
                            int week = Integer.parseInt(period[0]);
                            int time = Integer.parseInt(period[1]);
                            //获得所有能容纳当前班级人数的教师
                            List<Classroom> classroomList = classroomDao.getClassroomListByHoldNum(classes.getStudentNum());
                            classroomLoop:for (Classroom classroom : classroomList) {
                                timetable.setClassroomId(classroom.getId());
                                int index = 11;
                                //遍历时间段，找到一个老师和班级都空闲的时间
                                while(index < 59) {
                                    if(index % 10 == 0) {
                                        index ++;
                                    }else {
                                        Timeslot timeslot = timeslotDao.getTimeslotById(String.valueOf(index));
                                        timetable.setTimeslotId(timeslot.getId());
                                        timetable.setWeeks("1");
                                        timetable.setId(UUIDUtil.getUUID());
                                        System.out.println("正在排课->{专业："+ major.getName()+
                                                "，年级："+grades[i]+",班级："+classes.getClassNo()+
                                                "，课程："+course.getName()+"，教师："+teacher.getName()+
                                                "，教室："+classroom.toString()+"，时间"+timeslot.toString()+"}");
                                        try {
                                            saveTimetable(timetable);
                                            break classroomLoop;
                                        } catch (ArrangeCourseException e) {
                                            System.out.println(e.getMessage());
                                            if (e.getMessage().equals("教室被占用")) {
                                                continue classroomLoop;
                                            } else if(e.getMessage().equals("该班级已经上过该课程")){
                                                break classroomLoop;
                                            }else if(e.getMessage().contains("无法按时完成课时任务")){
                                                index = ((index / 10) + 1) * 10 + 1;
                                            }else {
                                                if(time == 2){
                                                    index+=time;
                                                }else {
                                                    index+=4;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Integer> getWorkTimeByTeacher(String week,String tName) {
        return timetableDao.getWorkTimeByTeacher(week,tName);
    }

    @Override
    public Map<String, Integer> getWorkTime(String week) {
        return timetableDao.getWorkTime(week);
    }
}
