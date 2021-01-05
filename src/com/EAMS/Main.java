package com.EAMS;

import com.EAMS.controller.*;
import com.EAMS.domain.Course;
import com.EAMS.domain.Teacher;
import com.EAMS.domain.User;
import com.EAMS.util.JdbcUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private static User user;
    private final Image icon = new Image("/img/icon/icon.png");

    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        Main.user = user;
    }

    public void initLoginFrame() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/loginFrame.fxml"));
            AnchorPane root = loader.load();

            primaryStage = new Stage();
            Scene scene = new Scene(root);
            primaryStage.setTitle("登录");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(icon);

            LoginController controller = loader.getController();
            controller.setStage(primaryStage);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initRegisterFrame(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/registerFrame.fxml"));
            AnchorPane root = loader.load();

            Stage registerFrameStage = new Stage();
            Scene scene = new Scene(root);
            registerFrameStage.setTitle("注册");
            registerFrameStage.setScene(scene);
            registerFrameStage.setResizable(false);
            registerFrameStage.getIcons().add(icon);

            RegisterController controller = loader.getController();
            controller.setStage(registerFrameStage);

            registerFrameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTeacherManageModal(Teacher teacher, boolean isUpdate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/teacherManageModal.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);

            TeacherManageModalController controller = loader.getController();
            controller.setStage(stage);
            controller.setUpdated(isUpdate);
            controller.setTeacher(teacher);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initTeacherManageRelation(Teacher teacher) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/teacherCourseRelation.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);

            TeacherCourseRelationController controller = loader.getController();
            controller.setTeacher(teacher);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initCourseArrangeModal() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/courseArrangeModal.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);

            CourseArrangeModalController controller = loader.getController();
            controller.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initCourseManageRelation(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/courseMajorRelation.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);

            CourseMajorRelationController controller = loader.getController();
            controller.setCourse(course);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initCourseManageModal() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/courseManageModal.fxml"));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(icon);

            CourseManageModalController controller = loader.getController();
            controller.setStage(stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initMainFrame(User user){
        try {
            setUser(user);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/mainFrame.fxml"));
            AnchorPane root = loader.load();

            Stage mainFrameStage = new Stage();
            mainFrameStage.setTitle("教务辅助管理系统");
            mainFrameStage.getIcons().add(icon);
            mainFrameStage.setResizable(false);
            mainFrameStage.setAlwaysOnTop(false);
//            mainFrameStage.initModality(Modality.APPLICATION_MODAL);
            mainFrameStage.initOwner(primaryStage);

            Scene scene = new Scene(root);
            mainFrameStage.setScene(scene);
            mainFrameStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane initHomePane(){
        AnchorPane root = new AnchorPane();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/homePane.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    public AnchorPane initCourseArrangePane(){
        AnchorPane root = new AnchorPane();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/courseArrangePane.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    public AnchorPane initCourseManagePane(){
        AnchorPane root = new AnchorPane();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/courseManagePane.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    public AnchorPane initTeacherManagePane(){
        AnchorPane root = new AnchorPane();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/teacherManagePane.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public static void main(String[] args) {
        JdbcUtil.getCon();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initLoginFrame();
//
//        User u = new User();
//        u.setName("test");
//        u.setPhone("123456");
//        u.setEmail("test@123.com");
//        u.setInstitute("信工学院");
//        u.setSex("男");
//        initMainFrame(u);
    }

}
