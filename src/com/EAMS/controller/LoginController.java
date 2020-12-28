package com.EAMS.controller;


import com.EAMS.Main;
import com.EAMS.domain.User;
import com.EAMS.exception.LoginException;
import com.EAMS.service.UserService;
import com.EAMS.service.impl.UserServiceImpl;
import com.EAMS.util.AppearanceUtil;
import com.EAMS.util.LegalUtil;
import com.EAMS.util.MD5Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button registerBtn,loginBtn;

    @FXML
    private PasswordField password;

    @FXML
    private Label format_password,passwordLb,format_account,accountLb,systemLb;

    @FXML
    private TextField account;

    @FXML
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){
        LegalUtil.addMyListener(account,format_account);
        LegalUtil.addMyListener(password,format_password);
        Labeled[] labeleds = new Labeled[]{systemLb, accountLb, passwordLb, loginBtn, registerBtn};
        String[] imagePaths = new String[]{"/img/icon/icon.png", "/img/icon/account.png", "/img/icon/password.png",
                "/img/icon/submit.png", "/img/icon/register.png"};
        AppearanceUtil.setLabeledImage(labeleds, imagePaths);
    }


    @FXML
    void login(ActionEvent event) {
        String inAccount = account.getText().trim();
        String inPassword = password.getText().trim();
        String md5Password = MD5Util.getMD5(inPassword);
        System.out.println(inAccount);
        System.out.println(md5Password);

        if(LegalUtil.isEmpty(inAccount,format_account,"请输入用户名") |
           LegalUtil.isEmpty(inPassword,format_password,"请输入密码")){
            return;
        }

        UserService userService = new UserServiceImpl();
        try {
            User user = userService.login(inAccount,md5Password);
            stage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("登录成功");
            alert.headerTextProperty().set("欢迎你，"+user.getName());
            alert.showAndWait();
            new Main().initMainFrame(user);

        } catch (LoginException e) {
            e.printStackTrace();
            String msg = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.titleProperty().set("登录失败");
            alert.headerTextProperty().set(msg);
            alert.show();
            password.setText("");
            password.requestFocus();
        }
    }

    @FXML
    void register(ActionEvent event) {
        stage.close();
        new Main().initRegisterFrame();
    }


}
