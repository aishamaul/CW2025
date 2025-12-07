package com.comp2042.ui.view.menu;

import com.comp2042.ui.render.BrickStyler;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Provides background animations for the Challenge Mode menu screens.
 * <p>
 *      Includes falling bricks effects
 *      to celebrate level and challenge completion.
 * </p>
 */
public class ChallengeMenuAnimator {

    private final Random random = new Random();
    private final BrickStyler brickStyler = new BrickStyler();

    /**
     * Plays a "falling bricks" animation in the target pane.
     * @param targetPane The pane to spawn animations in.
     */
    public void playFallingBricks(Pane targetPane) {
        int count = 25;
        for (int i = 0; i < count; i++) {
            spawnFallingBrick(targetPane, i * 50);
        }
    }

    /**
     * Plays a "confetti explosion" animation in the target pane.
     * @param targetPane The pane to spawn animations in.
     */
    public void playConfetti(Pane targetPane) {
        int count = 60;
        for (int i = 0; i < count; i++) {
            spawnConfetti(targetPane);
        }
    }

    private void spawnFallingBrick(Pane parent, double delayMillis) {
        Rectangle rect = new Rectangle(20, 20);
        int colorCode = random.nextInt(7) + 1;
        brickStyler.applyBrickStyle(rect, colorCode);

        // random start position at top
        double startX = random.nextDouble() * parent.getWidth();
        rect.setTranslateX(startX);
        rect.setTranslateY(-50);
        rect.setOpacity(0);

        parent.getChildren().add(rect);

        // falling animation
        TranslateTransition fall = new TranslateTransition(Duration.seconds(2 + random.nextDouble()), rect);
        fall.setToY(parent.getHeight() + 50);
        fall.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fade = new FadeTransition(Duration.millis(500), rect);
        fade.setFromValue(0);
        fade.setToValue(1);

        RotateTransition rotate = new RotateTransition(Duration.seconds(2), rect);
        rotate.setByAngle(360);

        ParallelTransition all = new ParallelTransition(fall, fade, rotate);
        all.setDelay(Duration.millis(delayMillis));
        all.setOnFinished(e -> parent.getChildren().remove(rect));
        all.play();
    }

    private void spawnConfetti(Pane parent) {
        Rectangle particle = new Rectangle(8, 8);
        int colorCode = random.nextInt(9) + 1;
        brickStyler.applyBrickStyle(particle, colorCode);

        double centerX = parent.getWidth() / 2;
        double centerY = parent.getHeight() / 2;

        particle.setTranslateX(centerX);
        particle.setTranslateY(centerY);

        parent.getChildren().add(particle);

        // explosion vector
        double angle = random.nextDouble() * 360;
        double distance = 200 + random.nextDouble() * 300;
        double endX = centerX + Math.cos(Math.toRadians(angle)) * distance;
        double endY = centerY + Math.sin(Math.toRadians(angle)) * distance;

        TranslateTransition move = new TranslateTransition(Duration.seconds(1.5), particle);
        move.setToX(endX - centerX); // translate is relative to initial pos
        move.setToY(endY - centerY);
        move.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rotate = new RotateTransition(Duration.seconds(1.5), particle);
        rotate.setByAngle(720);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.0), particle);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(0.5));

        ParallelTransition anim = new ParallelTransition(move, rotate, fade);
        anim.setOnFinished(e -> parent.getChildren().remove(particle));
        anim.play();
    }
}
