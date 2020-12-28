package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.util.AppearanceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.Optional;


public class MainController {
    @FXML
    private HBox home,courseArranging,teacherManage,courseManage,exit;

    @FXML
    private Label homeLb,courseArrangingLb,teacherManageLb,courseManageLb,exitLb;

    @FXML
    private ImageView icon;

    @FXML
    private HBox[] boxes;

    @FXML
    private AnchorPane bodyPane;


    public void initialize(){
        boxes = new HBox[]{home, courseArranging, teacherManage, courseManage, exit};
        icon.setImage(new Image("/img/icon/icon.png"));
        Labeled[] labeleds = new Labeled[]{homeLb,courseArrangingLb,teacherManageLb,courseManageLb,exitLb};
        String[] imagePaths = new String[]{"/img/icon/home.png", "/img/icon/courseArranging.png", "/img/icon/teacher.png",
                "/img/icon/course.png", "/img/icon/exit.png"};
        AppearanceUtil.setLabeledImage(labeleds, imagePaths);
        AppearanceUtil.selected(home,boxes);
        AnchorPane homePane = new Main().initHomePane();
        bodyPane.getChildren().clear();
        bodyPane.getChildren().add(homePane);
    }

    @FXML
    void doHome(MouseEvent event) {
        AppearanceUtil.selected(home,boxes);
        AnchorPane homePane = new Main().initHomePane();
        bodyPane.getChildren().clear();
        bodyPane.getChildren().add(homePane);
    }

    @FXML
    void doCourseArranging(MouseEvent event) {
        AppearanceUtil.selected(courseArranging,boxes);
        AnchorPane homePane = new Main().initCourseArrangePane();
        bodyPane.getChildren().clear();
        bodyPane.getChildren().add(homePane);
    }

    @FXML
    void doTeacherManage(MouseEvent event) {
        AppearanceUtil.selected(teacherManage,boxes);
        AnchorPane homePane = new Main().initTeacherManagePane();
        bodyPane.getChildren().clear();
        bodyPane.getChildren().add(homePane);
    }

    @FXML
    void doCourseManage(MouseEvent event) {
        AppearanceUtil.selected(courseManage,boxes);
        AnchorPane homePane = new Main().initCourseManagePane();
        bodyPane.getChildren().clear();
        bodyPane.getChildren().add(homePane);
    }

    @FXML
    void doExit(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("退出系统");
        alert.setHeaderText("确认要退出吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.exit(0);
        }


    }
}
