package com.comp2042.ui.render;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BrickColor {

    public Paint getFillColor(int i) {
        Paint returnPaint;

        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.ORANGE;
                break;
            case 2:
                returnPaint = Color.ORANGERED;
                break;
            case 3:
                returnPaint = Color.YELLOW;
                break;
            case 4:
                returnPaint = Color.GREENYELLOW;
                break;
            case 5:
                returnPaint = Color.AQUA;
                break;
            case 6:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 7:
                returnPaint = Color.DEEPPINK;
                break;
            case 8:
                returnPaint = Color.GREY;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }
}

