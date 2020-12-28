package com.EAMS.controller;

import com.EAMS.domain.Institute;
import com.EAMS.domain.Teacher;
import com.EAMS.service.InstituteService;
import com.EAMS.service.TeacherService;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.service.impl.TeacherServiceImpl;
import com.EAMS.util.LegalUtil;
import com.EAMS.util.UUIDUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class TeacherManageModalController {
    @FXML
    private Stage stage;
    private boolean isUpdated;
    private Teacher teacher;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        if(isUpdated){
            System.out.println("1111");
            name.setText(teacher.getName());
            email.setText(teacher.getEmail());
            phone.setText(teacher.getPhone());
            if("女".equals(teacher.getSex())){
                sex_woman.setSelected(true);
            }
            for (int i = 0; i < instituteList.size(); i++) {
                if(isUpdated && teacher.getInstituteId().equals(instituteList.get(i).getId())){
                    institute.getSelectionModel().select(i);
                    return;
                }
            }
        }
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ToggleGroup sex;

    @FXML
    private Button submitBtn,closeBtn;

    @FXML
    private RadioButton sex_man,sex_woman;

    @FXML
    private ComboBox<?> institute;

    @FXML
    private TextField email,phone,name;

    @FXML
    private Label format_name,format_email,format_phone;

    List<Institute> instituteList;

    public void initialize(){
        LegalUtil.addMyListener(name,format_name);
        LegalUtil.addMyListener(email,format_email);
        LegalUtil.addMyListener(phone,format_phone);
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
        String inEmail = email.getText().trim();
        String inPhone = phone.getText().trim();

        Boolean format = true;
        String regex_email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String regex_phone = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        if(LegalUtil.isEmpty(inName,format_name,"姓名不能为空")){
            format = false;
        }
        if(!inEmail.matches(regex_email) && !"".equals(inEmail)){
            format_email.setText("请输入正确的邮箱");
            format = false;
        }
        if(!inPhone.matches(regex_phone) && !"".equals(inPhone)){
            format_phone.setText("请输入正确的手机号码");
            format = false;
        }
        if(!format){
            return;
        }
        String inSex = "男";
        if(sex_woman.isSelected()){
            inSex = "女";
        }

        String inInstitute = (String) institute.getSelectionModel().selectedItemProperty().getValue();
        String inInstituteId = "";
        for(Institute i : instituteList){
            if(inInstitute.equals(i.getName())){
                inInstituteId = i.getId();
            }
        }
        teacher.setName(inName);
        teacher.setSex(inSex);
        teacher.setInstituteId(inInstituteId);
        teacher.setEmail(inEmail);
        teacher.setPhone(inPhone);

        TeacherService teacherService = new TeacherServiceImpl();
        boolean flag = true;
        if(isUpdated){
            flag = teacherService.update(teacher);
        }else {
            teacher.setId(UUIDUtil.getUUID());
            flag = teacherService.save(teacher);
        }
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
