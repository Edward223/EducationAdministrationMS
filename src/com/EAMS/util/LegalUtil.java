package com.EAMS.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LegalUtil {
    //添加对textField监听器
    public static void addMyListener(TextField textField, Label label){
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                label.setText("");
            }
        });
    }

    //判断textField取得的数据text是否为空，若为空则在提示信息标签Label上添加信息msg
    public static boolean isEmpty(String text, Label label, String msg){
        boolean flag = false;
        if("".equals(text)){
            label.setText(msg);
            flag = true;
        }
        return flag;
    }
}
