package com.comp2042.ui.view.menu;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * Encapsulate the pulsing and rainbow glow animation for menu buttons
 */

public class MenuButtonAnimator {

    public void animateButtons(Node root) {
        if (root == null) {
            return;
        }
        root.lookupAll(".menuButton").forEach(node -> {
            if (node instanceof Button button) {
                applyPulse(button);
                applyRainbowGlow(button);
            }
        });
    }

    private void applyPulse(Button button) {
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.9), button);
        pulse.setFromX(1.0);
        pulse.setToX(1.07);
        pulse.setFromY(1.0);
        pulse.setToY(1.07);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    private void applyRainbowGlow(Button button) {
        DropShadow glow = new DropShadow();
        glow.setRadius(32);
        glow.setSpread(0.5);
        glow.setColor(Color.web("#ff8bd1"));
        button.setEffect(glow);

        Timeline rainbow = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.colorProperty(), Color.web("#ff8bd1"))),
                new KeyFrame(Duration.seconds(0.9), new KeyValue(glow.colorProperty(), Color.web("#7df2ff"))),
                new KeyFrame(Duration.seconds(1.8), new KeyValue(glow.colorProperty(), Color.web("#ffd96a"))),
                new KeyFrame(Duration.seconds(2.7), new KeyValue(glow.colorProperty(), Color.web("#9cfaa5"))),
                new KeyFrame(Duration.seconds(3.6), new KeyValue(glow.colorProperty(), Color.web("#c5a0ff")))
        );
        rainbow.setCycleCount(Animation.INDEFINITE);
        rainbow.setAutoReverse(true);
        rainbow.play();
    }
}
