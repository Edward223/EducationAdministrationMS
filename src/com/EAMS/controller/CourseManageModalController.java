package com.EAMS.controller;

import com.EAMS.domain.Course;
import com.EAMS.domain.Institute;
import com.EAMS.service.CourseService;
import com.EAMS.service.InstituteService;
import com.EAMS.service.impl.CourseServiceImpl;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.util.LegalUtil;
import com.EAMS.util.UUIDUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CourseManageModalController {
    @FXML
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button submitBtn,closeBtn;

    @FXML
    private TextField name,lesson,week;

    @FXML
    private Label format_period,format_name;

    @FXML
    private ComboBox<?> institute;

    List<Institute> instituteList;

    public void initialize(){
        LegalUtil.addMyListener(name,format_name);
        LegalUtil.addMyListener(lesson,format_period);
        LegalUtil.addMyListener(week,format_period);
        InstituteService instituteService = new InstituteServiceImpl();
        instituteList = instituteService.getInstituteList();
        if(instituteList == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("错误");
            alert.headerTextProperty().set("系统错误！");
            alert.show();
            this.stage.close();
        }

        String[] institutes = new String[instituteList.size()];
        for (int i = 0; i < institutes.length; i++) {
            institutes[i] = instituteList.get(i).getName();
        }

        institute.getItems().clear();
        ObservableList observableList = FXCollections.observableArrayList(institutes);
        institute.setItems(observableList);

        institute.getSelectionModel().selectFirst();
    }

    @FXML
    void submit(ActionEvent event) {
        String inName = name.getText().trim();
        String inWeek = week.getText().trim();
        String inLesson = lesson.getText().trim();

        Boolean format = true;
        String regex_period = "^\\d{1,2}$";
        if(!inWeek.matches(regex_period)){
            format_period.setText("周数规定在0~25以内");
        }else {
            int weeks = Integer.parseInt(inWeek);
            if(!(weeks>0 && weeks<25)){
                format_period.setText("周数规定在0~25以内");
                format = false;
            }
        }
        if(!inLesson.matches(regex_period)){
            format_period.setText("每周课时数规定在0~10以内");
        }else {
            int lessons = Integer.parseInt(inLesson);
            if(!(lessons>0 && lessons<10)){
                format_period.setText("每周课时数规定在0~10以内");
                format = false;
            }
        }
        if(LegalUtil.isEmpty(inName,format_name,"姓名不能为空")|
                LegalUtil.isEmpty(inLesson,format_period,"每周课时数不能为空")|
                LegalUtil.isEmpty(inWeek,format_period,"周数不能为空")){
            format = false;
        }
        if(!format){
            return;
        }
        String inInstitute = (String) institute.getSelectionModel().selectedItemProperty().getValue();
        String inInstituteId = "";
        for(Institute i : instituteList){
            if(inInstitute.equals(i.getName())){
                inInstituteId = i.getId();
            }
        }
        Course course = new Course();
        course.setId(UUIDUtil.getUUID());
        course.setName(inName);
        course.setInstituteId(inInstituteId);
        course.setPeriod(inWeek+"x"+inLesson);

        CourseService courseService = new CourseServiceImpl();
        boolean flag = courseService.save(course);
        if(!flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("系统错误");
            alert.show();
        }
        this.stage.close();
    }

    @FXML
    void close(ActionEvent event) {
        stage.close();
    }

}
