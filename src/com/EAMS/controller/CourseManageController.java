package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.domain.Course;
import com.EAMS.domain.Institute;
import com.EAMS.service.CourseService;
import com.EAMS.service.InstituteService;
import com.EAMS.service.impl.CourseServiceImpl;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.vo.CourseVo;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManageController {


    @FXML
    private TextField select_courseName;

    @FXML
    private TableColumn<CourseVo, String > idTabCol,teacherNumTabCol,deleteTabCol,courseTabCol,periodTabCol,instituteTabCol,nameTabCol,majorNumTabCol;

    @FXML
    private ImageView refreshImg;

    @FXML
    private TableView<CourseVo> tableView;

    @FXML
    private ComboBox<String> select_courserInstitute;

    private List<Institute> instituteList;

    public void initialize(){

        InstituteService instituteService = new InstituteServiceImpl();
        instituteList = instituteService.getInstituteList();
        if(instituteList != null){
            String[] institutes = new String[instituteList.size()+1];
            institutes[0] = "请选择学院";
            for (int i = 0; i < instituteList.size(); i++) {
                institutes[i+1] = instituteList.get(i).getName();
            }
            select_courserInstitute.getItems().clear();
            ObservableList observableList = FXCollections.observableArrayList(institutes);
            select_courserInstitute.setItems(observableList);
            select_courserInstitute.getSelectionModel().select(0);
        }

        refreshImg.setImage(new Image("/img/icon/reset.png"));

        selectBtn(new ActionEvent());
    }

    @FXML
    void selectBtn(ActionEvent event) {
        String selectTeacherName = select_courseName.getText().trim();
        String selectInstitute = select_courserInstitute.getSelectionModel().selectedItemProperty().getValue();
        Map<String ,String> map = new HashMap<>();
        if("请选择学院".equals(selectInstitute)) {
            selectInstitute="";
        }
        map.put("name",selectTeacherName);
        map.put("institute", selectInstitute);

        CourseService courseService = new CourseServiceImpl();
        List<CourseVo> courseList = courseService.getCourseVoList(map);
        showList(courseList);

    }

    @FXML
    void saveBtn(ActionEvent event) {
        new Main().initCourseManageModal();
        refreshBtn(null);
    }

    @FXML
    void refreshBtn(MouseEvent event) {
        select_courserInstitute.getSelectionModel().selectFirst();
        select_courseName.setText("");
        Map<String ,String> map = new HashMap<>();
        map.put("name","");
        map.put("institute","");
        CourseService courseService = new CourseServiceImpl();
        List<CourseVo> courseList = courseService.getCourseVoList(map);
        showList(courseList);
    }

    private void showList(List<CourseVo> courseList) {
        tableView.getItems().clear();
        ObservableList<CourseVo> observableList = FXCollections.observableArrayList(courseList);
        tableView.setItems(observableList);

        idTabCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTabCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        instituteTabCol.setCellValueFactory(new PropertyValueFactory<>("instituteName"));
        majorNumTabCol.setCellValueFactory(new PropertyValueFactory<>("majorNum"));
        teacherNumTabCol.setCellValueFactory(new PropertyValueFactory<>("teacherNum"));

        periodTabCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CourseVo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CourseVo, String> param) {
                String[] p = param.getValue().getPeriod().split("x");
                if(p.length!=2){
                    return new SimpleStringProperty("找不到数据");
                }
                SimpleStringProperty period = new SimpleStringProperty("每周"+p[1]+"课时（共"+p[0]+"周)");
                return period;
            }
        });

        deleteTabCol.setCellFactory((col) -> {
            TableCell<CourseVo, String> cell = new TableCell<CourseVo, String>() {
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
                            CourseService courseService = new CourseServiceImpl();
                            CourseVo courseVo = this.getTableView().getItems().get(this.getIndex());
                            if(courseService.deleteById(courseVo.getId())){
                                observableList.remove(courseVo);
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

        courseTabCol.setCellFactory((col) -> {
            TableCell<CourseVo, String> cell = new TableCell<CourseVo, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        Button courseBtn = new Button("排课");
                        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#286090"), new CornerRadii(20), Insets.EMPTY);
                        Background background = new Background(backgroundFill);
                        courseBtn.setBackground(background);
                        courseBtn.setTextFill(Paint.valueOf("#FFFFFF"));

                        this.setGraphic(courseBtn);
                        courseBtn.setOnMouseClicked((me) -> {
                            CourseVo CourseVo = this.getTableView().getItems().get(this.getIndex());
                            Course course = new Course();
                            course.setId(CourseVo.getId());
                            course.setName(CourseVo.getName());
                            course.setPeriod(CourseVo.getPeriod());
                            for(Institute i : instituteList){
                                if(CourseVo.getInstituteName().equals(i.getName())){
                                    course.setInstituteId(i.getId());
                                }
                            }
                            new Main().initCourseManageRelation(course);
                            refreshBtn(null);
                        });
                    }
                }
            };
            return cell;
        });

    }
}
