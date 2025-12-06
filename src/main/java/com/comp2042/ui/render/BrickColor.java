package com.comp2042.ui.render;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BrickColor {


    public void applyBrickStyle(Rectangle rectangle, int colorCode) {
        rectangle.getStyleClass().removeIf(name -> name.startsWith("brick-") || name.equals("brick"));
        rectangle.getStyleClass().add("brick");
        if (colorCode <= 0) {
            rectangle.getStyleClass().add("brick-empty");
        } else if (colorCode <= 9) {
            rectangle.getStyleClass().add("brick-" + colorCode);
        } else {
            rectangle.getStyleClass().add("brick-unknown");
        }
    }
}

