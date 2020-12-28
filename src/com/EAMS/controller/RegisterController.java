package com.EAMS.controller;

import com.EAMS.Main;
import com.EAMS.domain.Institute;
import com.EAMS.domain.User;
import com.EAMS.service.InstituteService;
import com.EAMS.service.UserService;
import com.EAMS.service.impl.InstituteServiceImpl;
import com.EAMS.service.impl.UserServiceImpl;
import com.EAMS.util.AppearanceUtil;
import com.EAMS.util.LegalUtil;
import com.EAMS.util.MD5Util;
import com.EAMS.util.UUIDUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class RegisterController {
    @FXML
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ToggleGroup sex;

    @FXML
    private Button registerBtn,resetBtn;

    @FXML
    private PasswordField password,confPassword;

    @FXML
    private RadioButton sex_man,sex_woman;

    @FXML
    private ComboBox<?> institute;

    @FXML
    private TextField account,email,phone,name;

    @FXML
    private Label format_confPassword,format_name,format_email,format_password,format_phone,format_account;

    public void initialize(){
        System.out.println("初始化注册窗口");
        LegalUtil.addMyListener(account,format_account);
        LegalUtil.addMyListener(password,format_password);
        LegalUtil.addMyListener(confPassword,format_confPassword);
        LegalUtil.addMyListener(name,format_name);
        LegalUtil.addMyListener(email,format_email);
        LegalUtil.addMyListener(phone,format_phone);
        Labeled[] labeleds = new Labeled[]{registerBtn, resetBtn};
        String[] imagePaths = new String[]{"/img/icon/submit.png", "/img/icon/reset.png"};
        AppearanceUtil.setLabeledImage(labeleds, imagePaths);
        InstituteService instituteService = new InstituteServiceImpl();
        List<Institute> instituteList = instituteService.getInstituteList();
        if(instituteList == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("错误");
            alert.headerTextProperty().set("系统错误！");
            alert.show();
            this.stage.close();
            new Main().initLoginFrame();
        }

        String[] institutes = new String[instituteList.size()];
        for (int i = 0; i < institutes.length; i++) {
            institutes[i] = instituteList.get(i).getName();
        }

        institute.getItems().clear();
        ObservableList observableList = FXCollections.observableArrayList(institutes);
        institute.setItems(observableList);
        institute.getSelectionModel().select(0);
    }

    @FXML
    void register(ActionEvent event) {
        String inAccount = account.getText().trim();
        String inPassword = password.getText().trim();
        String inConfPassword = confPassword.getText().trim();
        String inName = name.getText().trim();
        String inEmail = email.getText().trim();
        String inPhone = phone.getText().trim();

        Boolean format = true;
        String regex_email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        String regex_phone = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        if(LegalUtil.isEmpty(inAccount,format_account,"用户名不能为空") |
            LegalUtil.isEmpty(inPassword,format_password,"密码不能为空") |
            LegalUtil.isEmpty(inName,format_name,"姓名不能为空")){
            format = false;
        }
        if(!inPassword.equals(inConfPassword)){
            format_confPassword.setText("两次密码不一致");
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
        User user = new User();
        user.setId(UUIDUtil.getUUID());
        user.setAccount(inAccount);
        user.setPassword(MD5Util.getMD5(inPassword));
        user.setName(inName);
        user.setSex(inSex);
        user.setInstitute(inInstitute);
        user.setEmail(inEmail);
        user.setPhone(inPhone);
        user.setAuthority("1");

        UserService userService = new UserServiceImpl();
        boolean flag = userService.register(user);
        if(flag){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("注册成功");
            alert.showAndWait();
            stage.close();
            new Main().initLoginFrame();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.titleProperty().set("注册失败");
            alert.show();
        }
    }

    @FXML
    void reset(ActionEvent event) {
        account.setText("");
        password.setText("");
        confPassword.setText("");
        name.setText("");
        email.setText("");
        phone.setText("");
        sex_man.setSelected(true);
        institute.getSelectionModel().select(0);
    }
}
