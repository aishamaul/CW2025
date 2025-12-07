package com.comp2042.ui.components;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Handles the animation logic for floating notifications.
 * <p>
 * This class creates a float up and fade out effect
 * when points are awarded.
 * </p>
 */
public class NotificationAnimator {

    private static final Duration FADE_DURATION = Duration.millis(2000);
    private static final Duration TRANSLATE_DURATION = Duration.millis(2500);
    private static final double VERTICAL_OFFSET = 40.0;

    /**
     * Animates the provided notification node.
     * <p>
     *     The node will translate upwards while fading out
     * </p>
     * @param notificationNode The UI element to animate.
     * @param onFinished A callback to execute when the animation completes (e.g., to remove the node).
     */
    public void animate(Node notificationNode, Runnable onFinished) {
        FadeTransition fadeTransition = new FadeTransition(FADE_DURATION, notificationNode);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        TranslateTransition translateTransition = new TranslateTransition(TRANSLATE_DURATION, notificationNode);
        translateTransition.setToY(notificationNode.getLayoutY() - VERTICAL_OFFSET);

        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> onFinished.run());
        parallelTransition.play();
    }
}
