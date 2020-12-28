package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.domain.Institute;
import com.EAMS.domain.Teacher;
import com.EAMS.service.InstituteService;
import com.EAMS.service.TeacherService;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.service.impl.TeacherServiceImpl;
import com.EAMS.vo.TeacherVo;
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

public class TeacherManageController {

    @FXML
    private TableColumn<TeacherVo, String> idTabCol,emailTabCol,sexTabCol,phoneTabCol,instituteTabCol,nameTabCol,deleteTabCol,updateTabCol,courseTabCol;

    @FXML
    private TableView<TeacherVo> tableView;

    @FXML
    private ComboBox<String> select_teacherSex,select_teacherInstitute;

    @FXML
    private TextField select_teacherName;

    @FXML
    private ImageView refreshImg;

    List<Institute> instituteList;

    public void initialize(){

        InstituteService instituteService = new InstituteServiceImpl();
        instituteList = instituteService.getInstituteList();
        if(instituteList != null){
            String[] institutes = new String[instituteList.size()+1];
            institutes[0] = "请选择学院";
            for (int i = 0; i < instituteList.size(); i++) {
                institutes[i+1] = instituteList.get(i).getName();
            }
            select_teacherInstitute.getItems().clear();
            ObservableList observableList = FXCollections.observableArrayList(institutes);
            select_teacherInstitute.setItems(observableList);
            select_teacherInstitute.getSelectionModel().select(0);
        }
        select_teacherSex.getItems().clear();
        select_teacherSex.getItems().addAll("请选择性别","男","女");
        select_teacherSex.getSelectionModel().selectFirst();

        refreshImg.setImage(new Image("/img/icon/reset.png"));
        selectBtn(new ActionEvent());

    }

    @FXML
    void selectBtn(ActionEvent event) {
        String selectTeacherName = select_teacherName.getText().trim();
        String selectTeacherSex = select_teacherSex.getSelectionModel().selectedItemProperty().getValue();
        String selectInstitute = select_teacherInstitute.getSelectionModel().selectedItemProperty().getValue();
        Map<String ,String> map = new HashMap<>();
        if("请选择学院".equals(selectInstitute)) {
            selectInstitute="";
        }
        if("请选择性别".equals(selectTeacherSex)) {
            selectTeacherSex="";
        }
        map.put("name",selectTeacherName);
        map.put("sex",selectTeacherSex);
        map.put("institute", selectInstitute);

        TeacherService teacherService = new TeacherServiceImpl();
        List<TeacherVo> teacherList = teacherService.getTeacherVoList(map);
        showList(teacherList);
    }

    @FXML
    void saveBtn(ActionEvent event) {
        new Main().initTeacherManageModal(new Teacher(),false);
        refreshBtn(null);
    }

    @FXML
    void refreshBtn(MouseEvent event) {
        select_teacherSex.getSelectionModel().selectFirst();
        select_teacherInstitute.getSelectionModel().selectFirst();
        select_teacherName.setText("");
        Map<String ,String> map = new HashMap<>();
        map.put("name","");
        map.put("sex","");
        map.put("institute","");
        TeacherService teacherService = new TeacherServiceImpl();
        List<TeacherVo> teacherList = teacherService.getTeacherVoList(map);
        showList(teacherList);
    }

    private void showList(List<TeacherVo> teacherList) {
        tableView.getItems().clear();
        ObservableList<TeacherVo> observableList = FXCollections.observableArrayList(teacherList);
        tableView.setItems(observableList);

        idTabCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailTabCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        sexTabCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        phoneTabCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        instituteTabCol.setCellValueFactory(new PropertyValueFactory<>("instituteName"));
        nameTabCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        deleteTabCol.setCellFactory((col) -> {
            TableCell<TeacherVo, String> cell = new TableCell<TeacherVo, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Button delBtn = new Button("删除");

                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#c9302c"), new CornerRadii(20), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        delBtn.setBackground(background);
                        delBtn.setTextFill(Paint.valueOf("#FFFFFF"));

                        this.setGraphic(delBtn);
                        delBtn.setOnMouseClicked((me) -> {
                            TeacherService teacherService = new TeacherServiceImpl();
                            TeacherVo teacherVo = this.getTableView().getItems().get(this.getIndex());
                            if(teacherService.deleteById(teacherVo.getId())){
                                observableList.remove(teacherVo);
                            }else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("删除失败");
                                alert.showAndWait();
                            }
                        });
                    }
                }

            };
            return cell;
        });
        updateTabCol.setCellFactory((col) -> {
            TableCell<TeacherVo, String> cell = new TableCell<TeacherVo, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        Button updateBtn = new Button("修改");

                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#5bc0de"), new CornerRadii(20), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        updateBtn.setBackground(background);
                        updateBtn.setTextFill(Paint.valueOf("#FFFFFF"));

                        this.setGraphic(updateBtn);
                        updateBtn.setOnMouseClicked((me) -> {
                            TeacherVo teacherVo = this.getTableView().getItems().get(this.getIndex());
                            Teacher teacher = new Teacher();
                            teacher.setId(teacherVo.getId());
                            teacher.setName(teacherVo.getName());
                            teacher.setSex(teacherVo.getSex());
                            teacher.setEmail(teacherVo.getEmail());
                            teacher.setPhone(teacherVo.getPhone());
                            for(Institute i : instituteList){
                                if(teacherVo.getInstituteName().equals(i.getName())){
                                    teacher.setInstituteId(i.getId());
                                }
                            }
                            new Main().initTeacherManageModal(teacher,true);
                            refreshBtn(null);
                        });
                    }
                }

            };
            return cell;
        });
        courseTabCol.setCellFactory((col) -> {
            TableCell<TeacherVo, String> cell = new TableCell<TeacherVo, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Button courseBtn = new Button("授课");
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#286090"), new CornerRadii(20), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        courseBtn.setBackground(background);
                        courseBtn.setTextFill(Paint.valueOf("#FFFFFF"));

                        this.setGraphic(courseBtn);
                        courseBtn.setOnMouseClicked((me) -> {
                            TeacherVo teacherVo = this.getTableView().getItems().get(this.getIndex());
                            Teacher teacher = new Teacher();
                            teacher.setId(teacherVo.getId());
                            teacher.setName(teacherVo.getName());
                            teacher.setSex(teacherVo.getSex());
                            teacher.setEmail(teacherVo.getEmail());
                            teacher.setPhone(teacherVo.getPhone());
                            for(Institute i : instituteList){
                                if(teacherVo.getInstituteName().equals(i.getName())){
                                    teacher.setInstituteId(i.getId());
                                }
                            }
                            new Main().initTeacherManageRelation(teacher);
                        });
                    }
                }
            };
            return cell;
        });

    }

}
