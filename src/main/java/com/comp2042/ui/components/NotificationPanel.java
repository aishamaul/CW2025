package com.comp2042.ui.components;

import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;

/**
 * A custom UI component representing a single notification popup.
 * <p>
 * It consists of a styled label with a glow effect, centered within a pane.
 * </p>
 */
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
