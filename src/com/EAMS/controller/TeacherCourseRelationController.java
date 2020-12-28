package com.EAMS.controller;

import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.TeacherCourseRelation;
import com.EAMS.service.CourseService;
import com.EAMS.service.TeacherService;
import com.EAMS.service.impl.CourseServiceImpl;
import com.EAMS.service.impl.TeacherServiceImpl;
import com.EAMS.util.UUIDUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Collection;
import java.util.List;

public class TeacherCourseRelationController {

    private Teacher teacher;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        infoLb.setText(teacher.getName()+"老师所授课程：");
        showList();
    }

    @FXML
    private ListView<Course> courseListView,allCourseListView;

    @FXML
    private Label infoLb;

    TeacherService teacherService =new TeacherServiceImpl();
    CourseService courseService = new CourseServiceImpl();

    private void showList() {
        List<Course> allCourseList = courseService.getCourseListByIid(teacher.getInstituteId());
        List<Course> courseList = teacherService.getCourseListByTid(teacher.getId());
        allCourseList.removeAll(courseList);

        ObservableList<Course> allCourseObservableList = FXCollections.observableList(allCourseList);
        ObservableList<Course> courseObservableList = FXCollections.observableList(courseList);

        allCourseListView.getItems().clear();
        courseListView.getItems().clear();

        allCourseListView.setItems(allCourseObservableList);
        courseListView.setItems(courseObservableList);

    }

    @FXML
    void unbund(ActionEvent event) {
        Course course = courseListView.getSelectionModel().getSelectedItem();
        System.out.println(course.getId()+"  "+course.getName());
        boolean flag = teacherService.unBundCourse(teacher.getId(),course.getId());
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("系统错误");
            alert.showAndWait();
        }
        showList();
    }

    @FXML
    void bund(ActionEvent event) {
        Course course = allCourseListView.getSelectionModel().getSelectedItem();
        System.out.println(course.getId()+"  "+course.getName());
        TeacherCourseRelation teacherCourseRelation = new TeacherCourseRelation();
        teacherCourseRelation.setId(UUIDUtil.getUUID());
        teacherCourseRelation.setCourseId(course.getId());
        teacherCourseRelation.setTeacherId(teacher.getId());
        boolean flag = teacherService.bundCourse(teacherCourseRelation);
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("系统错误");
            alert.showAndWait();
        }
        showList();
    }
}
