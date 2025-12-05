package com.comp2042.ui.components;

import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class NotificationPanel extends BorderPane {

    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().addAll("bonusStyle", "scoreClass");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        setCenter(score);
    }
}
