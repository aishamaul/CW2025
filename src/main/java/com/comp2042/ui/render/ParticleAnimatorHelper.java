package com.comp2042.ui.render;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ParticleAnimatorHelper {

    /**
     * Animates a node moving and fading out, then removes it from the parent pane.
     *
     * @param node        The node to animate.
     * @param parent      The parent pane to remove the node from after animation.
     * @param duration    The duration of the movement and fade.
     * @param moveX       The X distance to translate.
     * @param moveY       The Y distance to translate.
     * @param fadeDelay   Delay before fading starts.
     * @param interpolator The interpolator for the movement.
     */

    public static void animateDriftAndFade(Node node, Pane parent, Duration duration,
                                           double moveX, double moveY,
                                           Duration fadeDelay, Interpolator interpolator) {

        TranslateTransition move = new TranslateTransition(duration, node);
        move.setByX(moveX);
        move.setByY(moveY);
        move.setInterpolator(interpolator);

        FadeTransition fade = new FadeTransition(duration, node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        if (fadeDelay != null && !fadeDelay.equals(Duration.ZERO)) {
            fade.setDelay(fadeDelay);
        }

        ParallelTransition anim = new ParallelTransition(move, fade);
        anim.setOnFinished(ev -> parent.getChildren().remove(node));
        anim.play();
    }
}
