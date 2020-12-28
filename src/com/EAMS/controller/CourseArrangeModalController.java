package com.EAMS.controller;

import com.EAMS.domain.*;
import com.EAMS.exception.ArrangeCourseException;
import com.EAMS.service.*;
import com.EAMS.service.impl.*;
import com.EAMS.util.DateTimeUtil;
import com.EAMS.util.UUIDUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class CourseArrangeModalController {
    @FXML
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ListView<Course> courseListView;

    @FXML
    private ListView<Teacher> teacherListView;

    @FXML
    private ComboBox<String> grade,major,classNo,institute,classroom,time,day,week;

    public void initialize(){
        major.setDisable(true);
        grade.setDisable(true);
        classNo.setDisable(true);
        InstituteService instituteService = new InstituteServiceImpl();
        List<Institute> instituteList = instituteService.getInstituteList();
        if(instituteList != null){
            String[] institutes = new String[instituteList.size()+1];
            institutes[0] = "请选择学院";
            for (int i = 0; i < instituteList.size(); i++) {
                institutes[i+1] = instituteList.get(i).getName();
            }
            institute.getItems().clear();
            ObservableList instituteObservableList = FXCollections.observableArrayList(institutes);
            institute.setItems(instituteObservableList);
            institute.getSelectionModel().select(0);
        }
        institute.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                major.getItems().clear();
                grade.getItems().clear();
                classNo.getItems().clear();
                courseListView.getItems().clear();
                teacherListView.getItems().clear();
                grade.setDisable(true);
                classNo.setDisable(true);
                if(newVal==null || newVal.toString().equals("请选择学院")){
                    major.setDisable(true);
                }else {
                    major.setDisable(false);
                    MajorService majorService = new MajorServiceImpl();
                    List<Major> majorList = majorService.getMajorListByIname(newVal.toString());
                    if(majorList != null){
                        String[] majors = new String[majorList.size()+1];
                        majors[0] = "请选择专业";
                        for (int i = 0; i < majorList.size(); i++) {
                            majors[i+1] = majorList.get(i).getName();
                        }
                        major.getItems().clear();
                        ObservableList majorObservableList = FXCollections.observableArrayList(majors);
                        major.setItems(majorObservableList);
                        major.getSelectionModel().select(0);
                    }
                }
            }
        });
        major.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                grade.getItems().clear();
                classNo.getItems().clear();
                courseListView.getItems().clear();
                teacherListView.getItems().clear();
                classNo.setDisable(true);
                if(newVal==null || newVal.toString().equals("请选择专业")){
                    grade.setDisable(true);
                }else {
                    grade.setDisable(false);
                    String[] grades = DateTimeUtil.getInSchoolGrade();
                    ObservableList gradeObservableList = FXCollections.observableArrayList(grades);
                    grade.setItems(gradeObservableList);
                    grade.getSelectionModel().select(0);
                }
            }
        });
        grade.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                classNo.getItems().clear();
                courseListView.getItems().clear();
                teacherListView.getItems().clear();
                if(newVal==null || newVal.toString().equals("请选择年级")){
                    classNo.setDisable(true);
                }else {
                    classNo.setDisable(false);
                    ClassesService classesService = new ClassesServiceImpl();
                    String majorName = major.getSelectionModel().getSelectedItem();
                    String year = grade.getSelectionModel().getSelectedItem();
                    List<Classes> classesList = classesService.getClassListByCondition(majorName,year);
                    if(classesList != null){
                        String[] classes = new String[classesList.size()+1];
                        classes[0] = "请选择班级";
                        for (int i = 0; i < classesList.size(); i++) {
                            classes[i+1] = classesList.get(i).getClassNo();
                        }
                        classNo.getItems().clear();
                        ObservableList classObservableList = FXCollections.observableArrayList(classes);
                        classNo.setItems(classObservableList);
                        classNo.getSelectionModel().select(0);
                    }
                }
            }
        });
        classNo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                if(newVal==null || newVal.toString().equals("请选择班级")){
                    courseListView.getItems().clear();
                    teacherListView.getItems().clear();
                }else {
                    String year = grade.getSelectionModel().getSelectedItem();
                    String majorName = major.getSelectionModel().getSelectedItem();
                    String[] current = DateTimeUtil.getCurrentGrade(Integer.parseInt(year));
                    CourseService courseService = new CourseServiceImpl();
                    List<Course> courseList = courseService.getCoursePlanListByClass(majorName,current[0],current[1]);
                    ObservableList<Course> courseObservableList = FXCollections.observableList(courseList);
                    courseListView.getItems().clear();
                    courseListView.setItems(courseObservableList);
                }
            }
        });
        courseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Course>() {
            @Override
            public void changed(ObservableValue<? extends Course> observable, Course oldValue, Course newValue) {
                if(newValue != null){
                    TeacherService teacherService = new TeacherServiceImpl();
                    List<Teacher> teacherList = teacherService.getTeacherListByCid(newValue.getId());
                    ObservableList<Teacher> teacherObservableList = FXCollections.observableList(teacherList);
                    teacherListView.getItems().clear();
                    teacherListView.setItems(teacherObservableList);
                }
            }
        });
        ClassroomService classroomService = new ClassroomServiceImpl();
        List<Classroom> classroomList = classroomService.getClassroomList();
        if(classroomList != null){
            String[] classrooms = new String[classroomList.size()+1];
            for (int i = 0; i < classroomList.size(); i++) {
                classrooms[i] = classroomList.get(i).toString();
            }
            classroom.getItems().clear();
            ObservableList classroomObservableList = FXCollections.observableArrayList(classrooms);
            classroom.setItems(classroomObservableList);
            classroom.getSelectionModel().select(0);
        }
        week.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        week.getSelectionModel().selectFirst();
        day.getItems().addAll("周一","周二","周三","周四","周五");
        day.getSelectionModel().selectFirst();
        time.getItems().addAll("第1节课","第2节课","第3节课","第4节课","第5节课","第6节课","第7节课","第8节课","第9节课");
        time.getSelectionModel().selectFirst();
    }

    @FXML
    void submit(ActionEvent event) {
        String saveMajor = major.getSelectionModel().getSelectedItem();
        String saveYear = grade.getSelectionModel().getSelectedItem();
        String saveClassNo = classNo.getSelectionModel().getSelectedItem();
        ClassesService classesService = new ClassesServiceImpl();
        Classes saveClasses = classesService.getClassByCondition(saveMajor,saveYear,saveClassNo);

        Course saveCourse = courseListView.getSelectionModel().getSelectedItem();
        Teacher saveTeacher = teacherListView.getSelectionModel().getSelectedItem();

        String saveClassroom = classroom.getSelectionModel().getSelectedItem();
        ClassroomService classroomService = new ClassroomServiceImpl();
        Classroom saveRoom = classroomService.getClassroomByCondition(saveClassroom);

        String saveBeginWeek = week.getSelectionModel().getSelectedItem();
        String saveDay = day.getSelectionModel().getSelectedItem();
        String saveTime = time.getSelectionModel().getSelectedItem();
        TimetableService timetableService = new TimetableServiceImpl();
        Timeslot saveTimeslot = timetableService.getTimeslotByCondition(saveDay,saveTime);

        boolean formatFlag = true;
        StringBuilder msg = new StringBuilder("");
        if(saveClasses == null){
            msg.append("班级信息 ");
            formatFlag = false;
        }
        if(saveCourse == null){
            msg.append("课程信息 ");
            formatFlag = false;
        }
        if(saveTeacher == null){
            msg.append("教师信息 ");
            formatFlag = false;
        }
        if(saveRoom == null){
            msg.append("教室信息 ");
            formatFlag = false;
        }
        if(saveRoom == null){
            msg.append("教室信息 ");
            formatFlag = false;
        }
        if(!formatFlag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("输入信息错误");
            alert.setHeaderText("以下信息有误");
            alert.setContentText(msg.toString());
            alert.showAndWait();
        }else {
            Timetable timetable = new Timetable();
            timetable.setId(UUIDUtil.getUUID());
            timetable.setClassId(saveClasses.getId());
            timetable.setTeacherId(saveTeacher.getId());
            timetable.setClassroomId(saveRoom.getId());
            timetable.setCourseId(saveCourse.getId());
            timetable.setTimeslotId(saveTimeslot.getId());
            timetable.setWeeks(saveBeginWeek);
            boolean saveFlag = true;
            String exceptionMsg = "";
            try {
                saveFlag = timetableService.saveTimetable(timetable);
            } catch (ArrangeCourseException e) {
                e.printStackTrace();
                saveFlag = false;
                exceptionMsg = e.getMessage();
            }
            if(saveFlag){
                this.stage.close();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("添加失败");
                alert.setContentText(exceptionMsg);
                alert.showAndWait();
            }
        }

    }

    @FXML
    void quit(ActionEvent event) {
        this.stage.close();
    }

}
