package com.EAMS.util;

import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class AppearanceUtil {
    public static void setLabeledImage(Labeled[] labeleds, String[] imagePaths) {
        for (int i = 0; i < labeleds.length; i++) {
            Image image = new Image(imagePaths[i],labeleds[i].getPrefHeight()-5,labeleds[i].getPrefHeight()-5,false,true);
            labeleds[i].setGraphic(new ImageView(image));
        }
    }

    public static void selected(HBox selectedBox, HBox[] boxes){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i] == selectedBox) {
                boxes[i].setBackground(new Background(new BackgroundFill(Color.web("#83AEE4"),null,null)));
            }else {
                boxes[i].setBackground(new Background(new BackgroundFill(Color.web("#99ccff"),null,null)));
            }
        }

    }
}
