package com.EAMS.controller;

import com.EAMS.domain.*;
import com.EAMS.service.CourseService;
import com.EAMS.service.InstituteService;
import com.EAMS.service.MajorService;
import com.EAMS.service.impl.CourseServiceImpl;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.service.impl.MajorServiceImpl;
import com.EAMS.util.UUIDUtil;
import com.EAMS.vo.CoursePlanVo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CourseMajorRelationController {

    private Course course;

    public void setCourse(Course course) {
        this.course = course;
        infoLb.setText("《 "+course.getName()+" 》");
        showTableView();
    }

    @FXML
    private ComboBox<String> grade,semester;

    @FXML
    private TableColumn<CourseMajorRelation, String> majorTabCol,semesterTabCol,gradeTabCol;

    @FXML
    private TreeView<String> majorTreeView;

    @FXML
    private Label infoLb;

    @FXML
    private TableView<CoursePlanVo> planTableVIew;

    public void initialize(){
        String[] grades = new String[]{"1","2","3","4"};
        ObservableList<String> gradeObservableList = FXCollections.observableArrayList(grades);
        grade.setItems(gradeObservableList);
        String[] semesters = new String[]{"1","2"};
        ObservableList<String> semesterObservableList = FXCollections.observableArrayList(semesters);
        semester.setItems(semesterObservableList);
        grade.getSelectionModel().selectFirst();
        semester.getSelectionModel().selectFirst();

        InstituteService instituteService = new InstituteServiceImpl();
        MajorService majorService = new MajorServiceImpl();
        List<Institute> instituteList = instituteService.getInstituteList();

        TreeItem<String> root = new CheckBoxTreeItem<>();
        majorTreeView.setRoot(root);
        majorTreeView.setShowRoot(false);
        for(Institute i : instituteList){
            TreeItem<String> iItem = new TreeItem<>(i.getName());

            List<Major> majorList = majorService.getMajorListByIid(i.getId());
            for(Major m : majorList){
                TreeItem<String> mItem = new TreeItem<>(m.getName());
                iItem.getChildren().add(mItem);
            }
            root.getChildren().add(iItem);
        }
    }

    private void showTableView() {
        CourseService courseService = new CourseServiceImpl();
        List<CoursePlanVo> coursePlanList = courseService.getPlanById(course.getId());

        ObservableList<CoursePlanVo> coursePlanListObservableList = FXCollections.observableList(coursePlanList);
        planTableVIew.getItems().clear();
        planTableVIew.setItems(coursePlanListObservableList);

        majorTabCol.setCellValueFactory(new PropertyValueFactory<>("majorName"));
        gradeTabCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        semesterTabCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
    }

    @FXML
    void unbund(ActionEvent event) {
        CoursePlanVo coursePlanVo = planTableVIew.getSelectionModel().getSelectedItem();
        System.out.println(coursePlanVo.getId()+"  "+coursePlanVo.getMajorName()+"  "+coursePlanVo.getGrade());
        CourseService courseService = new CourseServiceImpl();
        boolean flag = courseService.deletePlanById(coursePlanVo.getId());
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("系统错误");
            alert.showAndWait();
        }
        showTableView();
    }

    @FXML
    void bund(ActionEvent event) {
        String inMajor = majorTreeView.getSelectionModel().getSelectedItem().getValue();
        String inGrade = grade.getSelectionModel().selectedItemProperty().getValue();
        String inSemester = semester.getSelectionModel().selectedItemProperty().getValue();
        System.out.println(inMajor+inGrade+inSemester);

        CourseService courseService = new CourseServiceImpl();
        CourseMajorRelation courseMajorRelation = new CourseMajorRelation();
        MajorService majorService = new MajorServiceImpl();
        String majorId = majorService.getMajorIdByName(inMajor);
        courseMajorRelation.setId(UUIDUtil.getUUID());
        courseMajorRelation.setCourseId(this.course.getId());
        courseMajorRelation.setGrade(inGrade);
        courseMajorRelation.setMajorId(majorId);
        courseMajorRelation.setSemester(inSemester);
        boolean flag = courseService.savePlan(courseMajorRelation);
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("系统错误");
            alert.showAndWait();
        }
        showTableView();
    }
}
