package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.domain.*;
import com.EAMS.service.*;
import com.EAMS.service.impl.*;
import com.EAMS.util.DateTimeUtil;
import com.EAMS.vo.TimetableVo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseArrangeController {
    @FXML
    private TableColumn<TimetableVo, String> classroomTabCol,teacherTabCol,deleteTabCol,classTabCol,majorTabCol,weekTabCol,idTabCol;
    @FXML
    private TableColumn<TimetableVo, String> courseTabCol,timeslotTabCol,updateTabCol;

    @FXML
    private ComboBox<String> select_major,select_institute,select_week,select_grade,select_classroom,select_class;

    @FXML
    private TextField select_course,select_teacher;

    @FXML
    private TableView<TimetableVo> tableView;

    @FXML
    private ImageView refreshImg;

    List<Institute> instituteList;
    List<Classroom> classroomList;

    public void initialize(){
        refreshImg.setImage(new Image("/img/icon/reset.png"));
        select_major.setDisable(true);
        select_grade.setDisable(true);
        select_class.setDisable(true);

        ClassroomService classroomService = new ClassroomServiceImpl();
        classroomList = classroomService.getClassroomList();
        if(classroomList != null){
            String[] classrooms = new String[classroomList.size()+1];
            classrooms[0] = "请选择教室";
            for (int i = 0; i < classroomList.size(); i++) {
                classrooms[i+1] = classroomList.get(i).toString();
            }
            select_classroom.getItems().clear();
            ObservableList classroomObservableList = FXCollections.observableArrayList(classrooms);
            select_classroom.setItems(classroomObservableList);
            select_classroom.getSelectionModel().select(0);
        }

        select_week.getItems().clear();
        select_week.getItems().addAll("请选择周次","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        select_week.getSelectionModel().selectFirst();

        InstituteService instituteService = new InstituteServiceImpl();
        instituteList = instituteService.getInstituteList();
        if(instituteList != null){
            String[] institutes = new String[instituteList.size()+1];
            institutes[0] = "请选择学院";
            for (int i = 0; i < instituteList.size(); i++) {
                institutes[i+1] = instituteList.get(i).getName();
            }
            select_institute.getItems().clear();
            ObservableList instituteObservableList = FXCollections.observableArrayList(institutes);
            select_institute.setItems(instituteObservableList);
            select_institute.getSelectionModel().select(0);
        }
        select_institute.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                select_major.getItems().clear();
                select_grade.getItems().clear();
                select_class.getItems().clear();
                select_grade.setDisable(true);
                select_class.setDisable(true);
                if(newVal==null || newVal.toString().equals("请选择学院")){
                    select_major.setDisable(true);
                }else {
                    select_major.setDisable(false);
                    MajorService majorService = new MajorServiceImpl();
                    List<Major> majorList = majorService.getMajorListByIname(newVal.toString());
                    if(majorList != null){
                        String[] majors = new String[majorList.size()+1];
                        majors[0] = "请选择专业";
                        for (int i = 0; i < majorList.size(); i++) {
                            majors[i+1] = majorList.get(i).getName();
                        }
                        select_major.getItems().clear();
                        ObservableList majorObservableList = FXCollections.observableArrayList(majors);
                        select_major.setItems(majorObservableList);
                        select_major.getSelectionModel().select(0);
                    }
                }
            }
        });
        select_major.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                select_grade.getItems().clear();
                select_class.getItems().clear();
                select_class.setDisable(true);
                if(newVal==null || newVal.toString().equals("请选择专业")){
                    select_grade.setDisable(true);
                }else {
                    select_grade.setDisable(false);
                    String[] grades = DateTimeUtil.getInSchoolGrade();
                    ObservableList gradeObservableList = FXCollections.observableArrayList(grades);
                    select_grade.setItems(gradeObservableList);
                    select_grade.getSelectionModel().select(0);
                }
            }
        });
        select_grade.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldVal, Object newVal) {
                select_class.getItems().clear();
                if(newVal==null || newVal.toString().equals("请选择年级")){
                    select_class.setDisable(true);
                }else {
                    select_class.setDisable(false);
                    ClassesService classesService = new ClassesServiceImpl();
                    String majorName = select_major.getSelectionModel().getSelectedItem();
                    String grade = select_grade.getSelectionModel().getSelectedItem();
                    List<Classes> classesList = classesService.getClassListByCondition(majorName,grade);
                    if(classesList != null){
                        String[] classes = new String[classesList.size()+1];
                        classes[0] = "请选择班级";
                        for (int i = 0; i < classesList.size(); i++) {
                            classes[i+1] = classesList.get(i).getClassNo();
                        }
                        select_class.getItems().clear();
                        ObservableList classObservableList = FXCollections.observableArrayList(classes);
                        select_class.setItems(classObservableList);
                        select_class.getSelectionModel().select(0);
                    }
                }
            }
        });

        select(new ActionEvent());
    }

    @FXML
    void select(ActionEvent event) {
        String selectTeacher = select_teacher.getText().trim();
        String selectCourse = select_course.getText().trim();
        String selectClassroom = select_classroom.getSelectionModel().selectedItemProperty().getValue();
        String selectWeek = select_week.getSelectionModel().selectedItemProperty().getValue();
        String selectInstitute = select_institute.getSelectionModel().selectedItemProperty().getValue();
        String selectMajor = select_major.getSelectionModel().selectedItemProperty().getValue();
        String selectGrade = select_grade.getSelectionModel().selectedItemProperty().getValue();
        String selectClass = select_class.getSelectionModel().selectedItemProperty().getValue();

        Map<String ,String> map = new HashMap<>();
        String selectInstituteId = "";
        String selectMajorId = "";
        String selectYear = "";
        String selectClassNo = "";
        String selectClassroomId = "";

        if(!"请选择学院".equals(selectInstitute) && selectInstitute != null) {
            InstituteService instituteService = new InstituteServiceImpl();
            selectInstituteId = instituteService.getInstituteIdByName(selectInstitute);
            if(!"请选择专业".equals(selectMajor) && selectMajor != null) {
                MajorService majorService = new MajorServiceImpl();
                selectMajorId = majorService.getMajorIdByName(selectMajor);
                if(!"请选择年级".equals(selectGrade) && selectGrade != null) {
                    selectYear = selectGrade;
                    if(!"请选择班级".equals(selectClass) && selectClass != null) {
                        selectClassNo = selectClass;
                    }
                }
            }
        }
        if("请选择周次".equals(selectWeek)){
            selectWeek="";
        }
        if(!"请选择教室".equals(selectClassroom)){
            ClassroomService classroomService = new ClassroomServiceImpl();
            selectClassroomId = classroomService.getClassroomIdByCondition(selectClassroom);
        }
        map.put("institute", selectInstituteId);
        map.put("major", selectMajorId);
        map.put("teacher", selectTeacher);
        map.put("course", selectCourse);
        map.put("week", selectWeek);

        map.put("year", selectYear);
        map.put("classNo", selectClassNo);
        map.put("classroom", selectClassroomId);

        TimetableService timetableService = new TimetableServiceImpl();
        List<TimetableVo> timetableList = timetableService.getTimetableVoList(map);
        showList(timetableList);
    }

    @FXML
    void saveTimetable(ActionEvent event) {
        new Main().initCourseArrangeModal();
        refresh(null);
    }

    @FXML
    void refresh(MouseEvent event) {
        select_major.getItems().clear();
        select_grade.getItems().clear();
        select_class.getItems().clear();
        select_major.setDisable(true);
        select_grade.setDisable(true);
        select_class.setDisable(true);
        select_institute.getSelectionModel().selectFirst();
        select_week.getSelectionModel().selectFirst();
        select_classroom.getSelectionModel().selectFirst();
        select_teacher.setText("");
        select_course.setText("");
        select(null);
    }

    private void showList(List<TimetableVo> timetableList) {
        tableView.getItems().clear();
        ObservableList<TimetableVo> observableList = FXCollections.observableArrayList(timetableList);
        tableView.setItems(observableList);

        idTabCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseTabCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        majorTabCol.setCellValueFactory(new PropertyValueFactory<>("majorName"));
        classTabCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        teacherTabCol.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        classroomTabCol.setCellValueFactory(new PropertyValueFactory<>("classroomName"));
        weekTabCol.setCellValueFactory(new PropertyValueFactory<>("week"));
        timeslotTabCol.setCellValueFactory(new PropertyValueFactory<>("timeslot"));

        deleteTabCol.setCellFactory((col) -> {
            TableCell<TimetableVo, String> cell = new TableCell<TimetableVo, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Button delBtn = new Button("取消");

                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#c9302c"), new CornerRadii(20), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        delBtn.setBackground(background);
                        delBtn.setTextFill(Paint.valueOf("#FFFFFF"));

                        this.setGraphic(delBtn);
                        delBtn.setOnMouseClicked((me) -> {
                            TimetableService timetableService = new TimetableServiceImpl();
                            TimetableVo timetableVo = this.getTableView().getItems().get(this.getIndex());
                            if(timetableService.deleteById(timetableVo.getId())){
                                observableList.remove(timetableVo);
                            }else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("取消失败");
                                alert.showAndWait();
                            }
                        });
                    }
                }

            };
            return cell;
        });
    }

    @FXML
    void initTimetable(ActionEvent event) {
        TimetableService timetableService = new TimetableServiceImpl();
        boolean flag = timetableService.init();
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("初始化失败");
            alert.showAndWait();
        }
        select(null);
    }

    @FXML
    void autoArrangeCourse(ActionEvent event) {
        TimetableService timetableService = new TimetableServiceImpl();
        timetableService.autoArrangeCourse();
    }
}
