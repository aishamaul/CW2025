package com.comp2042.ui.render;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BrickColor {

    public Color getBaseColor(int i) {
        Paint returnPaint;

        switch (i) {
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.ORANGERED;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.GREENYELLOW;
            case 5:
                return Color.AQUA;
            case 6:
                return Color.BLUEVIOLET;
            case 7:
                return Color.DEEPPINK;
            case 8:
                return Color.GREY;
            case 9:
                return Color.RED;
            default:
                return Color.WHITE;
        }
    }

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

