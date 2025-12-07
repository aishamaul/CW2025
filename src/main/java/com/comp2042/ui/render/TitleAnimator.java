package com.comp2042.ui.render;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

/**
 * Animates the main title label of the application.
 * <p>
 * Applies a continuous rainbow gradient shift and a pulsating glow effect to the text.
 * </p>
 */
public class TitleAnimator {

    private static final String TITLE_ANIMATOR_KEY = "TitleAnimator";

    private static final Stop[] RAINBOW_STOPS = new Stop[]{
            new Stop(0.00, Color.web("#fff5fb")),
            new Stop(0.18, Color.web("#fff8ef")),
            new Stop(0.36, Color.web("#f7fbff")),
            new Stop(0.54, Color.web("#f2fff8")),
            new Stop(0.72, Color.web("#f1f6ff")),
            new Stop(1.00, Color.web("#fdf2ff"))
    };

    private final Label titleLabel;
    private final Timeline rainbowTimeline;
    private final Timeline glowTimeline;
    private final DoubleProperty gradientOffset = new SimpleDoubleProperty(0.0);
    private final DoubleProperty glowLevel = new SimpleDoubleProperty(0.9);

    private TitleAnimator(Label titleLabel) {
        this.titleLabel = titleLabel;

        titleLabel.setCache(true);
        titleLabel.setCacheHint(CacheHint.SPEED);
        titleLabel.setScaleX(1.02);
        titleLabel.setScaleY(1.02);

        applyGradient(0.0);
        applyGlow();

        gradientOffset.addListener((obs, oldVal, newVal) -> {
            applyGradient(newVal.doubleValue());
            applyGlow();
        });

        glowLevel.addListener((obs, oldVal, newVal) -> applyGlow());

        rainbowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(gradientOffset, 0.0, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(7.5),
                        new KeyValue(gradientOffset, 1.0, Interpolator.LINEAR))
        );
        rainbowTimeline.setCycleCount(Animation.INDEFINITE);

        glowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glowLevel, 0.82)),
                new KeyFrame(Duration.seconds(3.2), new KeyValue(glowLevel, 1.0))
        );
        glowTimeline.setAutoReverse(true);
        glowTimeline.setCycleCount(Animation.INDEFINITE);

        rainbowTimeline.play();
        glowTimeline.play();
    }

    public static void attach(Label titleLabel) {
        if (titleLabel == null) {
            return;
        }

        if (titleLabel.getProperties().containsKey(TITLE_ANIMATOR_KEY)) {
            return;
        }

        TitleAnimator animator = new TitleAnimator(titleLabel);
        titleLabel.getProperties().put(TITLE_ANIMATOR_KEY, animator);
    }

    private void applyGradient(double offset) {
        titleLabel.setTextFill(createGradient(offset));
    }

    private LinearGradient createGradient(double offset) {
        double start = offset % 1.0;
        return new LinearGradient(
                start, 0,
                start + 1.0, 0,
                true,
                CycleMethod.REPEAT,
                RAINBOW_STOPS
        );
    }

    private void applyGlow() {
        titleLabel.setEffect(createGlow(glowLevel.get(), gradientOffset.get()));
    }

    private Effect createGlow(double intensity, double huePosition) {
        double clamped = Math.min(1.0, Math.max(0.0, intensity));
        double hue = ((huePosition % 1.0) + 1.0) % 1.0 * 360.0;

        Color prismatic = Color.hsb(hue, 0.95, 1.0, 0.8 * clamped);
        Color secondary = Color.hsb((hue + 110.0) % 360.0, 0.9, 1.0, 0.7 * clamped);
        Color tertiary = Color.hsb((hue + 220.0) % 360.0, 0.85, 1.0, 0.55 * clamped);

        DropShadow textEdge = new DropShadow();
        textEdge.setRadius(6);
        textEdge.setSpread(0.18);
        textEdge.setColor(Color.color(1, 1, 1, 0.42 * clamped));

        DropShadow neonRing = new DropShadow();
        neonRing.setRadius(42);
        neonRing.setSpread(0.64);
        neonRing.setColor(prismatic);
        neonRing.setInput(textEdge);

        DropShadow halo = new DropShadow();
        halo.setRadius(76);
        halo.setSpread(0.5);
        halo.setColor(secondary);
        halo.setInput(neonRing);

        DropShadow farBloom = new DropShadow();
        farBloom.setRadius(120);
        farBloom.setSpread(0.46);
        farBloom.setColor(tertiary);
        farBloom.setInput(halo);

        return farBloom;
    }

    public void stop() {
        rainbowTimeline.stop();
        glowTimeline.stop();
    }
}