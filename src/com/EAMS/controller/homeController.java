package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.domain.Institute;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.User;
import com.EAMS.service.*;
import com.EAMS.service.impl.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Map;

public class homeController {
    @FXML
    private ImageView courseIcon,classroomIcon,classIcon,userIcon,teacherIcon;

    @FXML
    private Label name,sex,phone,institute,email;

    @FXML
    private Label classNum,courseNum,classroomNum,teacherNum;

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private ComboBox<String> instituteComBox,weekComBox,teacherComBox;

    ClassesService classesService = new ClassesServiceImpl();
    TeacherService teacherService = new TeacherServiceImpl();
    CourseService courseService = new CourseServiceImpl();
    ClassroomService classroomService = new ClassroomServiceImpl();
    InstituteService instituteService = new InstituteServiceImpl();
    TimetableService timetableService = new TimetableServiceImpl();
    public void initialize(){
        userIcon.setImage(new Image("/img/icon/account.png"));
        classIcon.setImage(new Image("/img/picture/class.png"));
        classroomIcon.setImage(new Image("/img/picture/course.png"));
        courseIcon.setImage(new Image("/img/picture/classroom.png"));
        teacherIcon.setImage(new Image("/img/picture/teacher.png"));
        User user = Main.getUser();
        name.setText(user.getName());
        sex.setText(user.getSex());
        phone.setText(user.getPhone());
        institute.setText(user.getInstitute());
        email.setText(user.getEmail());

        classNum.setText(classesService.getClassNum()+"个");
        teacherNum.setText(teacherService.getTeacherNum()+"人");
        courseNum.setText(courseService.getCourseNum()+"门");
        classroomNum.setText(classroomService.getClassroomNum()+"间");

        weekComBox.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        weekComBox.getSelectionModel().selectFirst();
        List<Institute> instituteList = instituteService.getInstituteList();
        if(instituteList != null){
            String[] institutes = new String[instituteList.size()+1];
            institutes[0] = "请选择学院";
            for (int i = 0; i < instituteList.size(); i++) {
                institutes[i+1] = instituteList.get(i).getName();
            }
            instituteComBox.getItems().clear();
            ObservableList instituteObservableList = FXCollections.observableArrayList(institutes);
            instituteComBox.setItems(instituteObservableList);
            instituteComBox.getSelectionModel().select(0);
        }
        instituteComBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                teacherComBox.getItems().clear();
                if(newVal==null || newVal.toString().equals("请选择学院")){
                    teacherComBox.setDisable(true);
                }else {
                    teacherComBox.setDisable(false);
                    List<Teacher> teacherList = teacherService.getTeacherByIname(newVal.toString());
                    if(teacherList != null){
                        String[] teachers = new String[teacherList.size()+1];
                        teachers[0] = "请选择教师";
                        for (int i = 0; i < teacherList.size(); i++) {
                            teachers[i+1] = teacherList.get(i).getName();
                        }
                        teacherComBox.getItems().clear();
                        ObservableList teacherObservableList = FXCollections.observableArrayList(teachers);
                        teacherComBox.setItems(teacherObservableList);
                        teacherComBox.getSelectionModel().select(0);
                    }
                }
            }
        });

        weekComBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                String teacherName = teacherComBox.getSelectionModel().getSelectedItem();
                if(teacherName==null ||teacherName.equals("请选择教师")){
                    Map<String,Integer> map = timetableService.getWorkTime(newVal.toString());
                    setPersonData(map);
                }else {
                    Map<String,Integer> map = timetableService.getWorkTimeByTeacher(newVal.toString(),teacherName);
                    setPersonData(map);
                }
            }
        });
        teacherComBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                String week = weekComBox.getSelectionModel().getSelectedItem();
                if(newVal==null || newVal.toString().equals("请选择教师")){
                    Map<String,Integer> map = timetableService.getWorkTime(week);
                    setPersonData(map);
                }else {
                    Map<String,Integer> map = timetableService.getWorkTimeByTeacher(week,newVal.toString());
                    setPersonData(map);
                }
            }
        });

        Map<String,Integer> map = timetableService.getWorkTime("1");
        setPersonData(map);
    }

    public void setPersonData(Map<String,Integer> map) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        barChart.setCategoryGap(100);
        barChart.getData().clear();

        series.getData().add(new XYChart.Data<>("周一",map.get("周一")));
        series.getData().add(new XYChart.Data<>("周二",map.get("周二")));
        series.getData().add(new XYChart.Data<>("周三",map.get("周三")));
        series.getData().add(new XYChart.Data<>("周四",map.get("周四")));
        series.getData().add(new XYChart.Data<>("周五",map.get("周五")));

        barChart.getData().add(series);
    }

}
