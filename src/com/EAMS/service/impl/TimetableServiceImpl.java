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

        //?????????????????????????????????
        if(timetableDao.getTimetableByClassAndCourse(clazz.getId(),course.getId()) != 0){
            throw new ArrangeCourseException("??????????????????????????????");
        }

        //??????????????????????????????????????????
        if(Integer.parseInt(classroom.getHoldNum()) < Integer.parseInt(clazz.getStudentNum())){
            throw new ArrangeCourseException("?????????????????????");
        }

        //??????20????????????????????????
        String[] period = course.getPeriod().split("x");
        int beginWeek = Integer.parseInt(timetable.getWeeks());
        int endWeek = beginWeek + Integer.parseInt(period[0]);
        if(endWeek > 21){
            throw new ArrangeCourseException("??????????????????????????????,???????????????"+period[0]+"??????");
        }

        //?????????????????????????????????????????????
        int beginTimeslot = Integer.parseInt(timetable.getTimeslotId());
        int endTimeslot = beginTimeslot + Integer.parseInt(period[1]);
        if(beginTimeslot%10 + Integer.parseInt(period[1]) > 10){
            throw new ArrangeCourseException("????????????????????????????????????????????????"+period[1]+"??????");
        }

        String[] weeks1 = new String[endWeek+beginWeek+1];
        for (int i = beginWeek; i <= endWeek; i++) {
            weeks1[i] = String.valueOf(i);
        }
        Set<String> WeekSet1 =  new HashSet<>(Arrays.asList(weeks1));//???????????????????????????????????????

        //?????????????????????????????????????????????
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week1 = timetableDao.getWeeksByRoomAndTimeslot(timetable.getClassroomId(),String.valueOf(i));
            String[] weeks2 = week1.split(",");
            Set<String> weekSet2 =  new HashSet<>(Arrays.asList(weeks2));//????????????????????????????????????????????????
            int before1 = weekSet2.size();
            weekSet2.removeAll(WeekSet1);
            if(before1 != weekSet2.size()){
                throw new ArrangeCourseException("???????????????");
            }
        }

        //?????????????????????????????????????????????
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week2 = timetableDao.getWeeksByTeacherAndTimeslot(timetable.getTeacherId(),String.valueOf(i));
            String[] weeks3 = week2.split(",");
            Set<String> weekSet3 =  new HashSet<>(Arrays.asList(weeks3));//????????????????????????????????????????????????
            int before2 = weekSet3.size();
            weekSet3.removeAll(WeekSet1);
            if(before2 != weekSet3.size()){
                throw new ArrangeCourseException("???????????????????????????????????????");
            }
        }

        //?????????????????????????????????????????????
        for (int i = beginTimeslot; i <= endTimeslot; i++) {
            String week3 = timetableDao.getWeeksByClassesAndTimeslot(timetable.getClassId(),String.valueOf(i));
            String[] weeks4 = week3.split(",");
            Set<String> weekSet4 =  new HashSet<>(Arrays.asList(weeks4));//????????????????????????????????????????????????
            int before3 = weekSet4.size();
            weekSet4.removeAll(WeekSet1);
            if(before3 != weekSet4.size()){
                throw new ArrangeCourseException("???????????????????????????????????????");
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

        //??????????????????
        List<Institute> instituteList = instituteDao.getInstituteList();
        for (Institute institute : instituteList) {
            //??????????????????
            List<Major> majorList = majorDao.getMajorListByIid(institute.getId());
            for (Major major : majorList) {
                String[] grades = DateTimeUtil.getInSchoolGrade();
                String semester = DateTimeUtil.getSemester();
                for (int i = 1; i <= 4; i++) {
                    //???????????????????????????????????????????????????????????????
                    List<Course> courseList = courseDao.getCoursePlanListByClass(major.getName(),String.valueOf(i),semester);
                    for (Course course : courseList) {
                        timetable.setCourseId(course.getId());

                        //?????????????????????????????????????????????
                        List<Classes> classesList = classesDao.getClassListByCondition(major.getName(),grades[i]);
                        for (Classes classes : classesList) {
                            timetable.setClassId(classes.getId());

                            //????????????????????????????????????????????????????????????????????????
                            Teacher teacher = teacherDao.getTeacherWithMinWorkloadByCid(course.getId());
                            timetable.setTeacherId(teacher.getId());

                            //???????????????????????????????????????
                            String[] period = course.getPeriod().split("x");
                            int week = Integer.parseInt(period[0]);
                            int time = Integer.parseInt(period[1]);
                            //????????????????????????????????????????????????
                            List<Classroom> classroomList = classroomDao.getClassroomListByHoldNum(classes.getStudentNum());
                            classroomLoop:for (Classroom classroom : classroomList) {
                                timetable.setClassroomId(classroom.getId());
                                int index = 11;
                                //???????????????????????????????????????????????????????????????
                                while(index < 59) {
                                    if(index % 10 == 0) {
                                        index ++;
                                    }else {
                                        Timeslot timeslot = timeslotDao.getTimeslotById(String.valueOf(index));
                                        timetable.setTimeslotId(timeslot.getId());
                                        timetable.setWeeks("1");
                                        timetable.setId(UUIDUtil.getUUID());
                                        System.out.println("????????????->{?????????"+ major.getName()+
                                                "????????????"+grades[i]+",?????????"+classes.getClassNo()+
                                                "????????????"+course.getName()+"????????????"+teacher.getName()+
                                                "????????????"+classroom.toString()+"?????????"+timeslot.toString()+"}");
                                        try {
                                            saveTimetable(timetable);
                                            break classroomLoop;
                                        } catch (ArrangeCourseException e) {
                                            System.out.println(e.getMessage());
                                            if (e.getMessage().equals("???????????????")) {
                                                continue classroomLoop;
                                            } else if(e.getMessage().equals("??????????????????????????????")){
                                                break classroomLoop;
                                            }else if(e.getMessage().contains("??????????????????????????????")){
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
