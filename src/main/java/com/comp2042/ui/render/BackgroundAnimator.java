package com.comp2042.ui.render;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Manages the dynamic background visual effects for the game menus and gameplay.
 * <p>
 * This class layers several visual elements: a radial gradient, a noise texture overlay,
 * floating particles, and a vignette effect. It handles the animation loops for these
 * elements to create an immersive atmosphere.
 * </p>
 */
public class BackgroundAnimator {

    private static final String BACKGROUND_KEY = "Background";

    private final StackPane target;
    private final Pane gradientLayer = new Pane();
    private final Pane vignetteLayer = new Pane();
    private final Canvas noiseCanvas = new Canvas();
    private final Group particleLayer = new Group();
    private Timeline shimmerTimeline;
    private final List<Timeline> particleTimelines = new ArrayList<>();

    private BackgroundAnimator(StackPane target) {
        this.target = target;
        configureLayers();
        attachLayers();
        bindSizes();
        refreshNoise();
        startAnimations();
    }

    public static void attach(StackPane target) {
        if (target == null) return;

        //prevent multiple BackgroundAnimator instances per targer
        if (target.getProperties().containsKey(BACKGROUND_KEY)) return;

        BackgroundAnimator background = new BackgroundAnimator(target);
        target.getProperties().put(BACKGROUND_KEY, background);
    }

    private void configureLayers() {

        // base gradient background
        gradientLayer.setBackground(new Background(new BackgroundFill(
                new RadialGradient(
                        0, 0,
                        0.5, 0.45,
                        1.0,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0.0, Color.web("#0b0f1a")),
                        new Stop(0.45, Color.web("#070a14")),
                        new Stop(1.0, Color.web("#010208"))



                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        // ignore mouse events so it doesn't block UI interaction
        gradientLayer.setMouseTransparent(true);

        vignetteLayer.setBackground(new Background(new BackgroundFill(
                new RadialGradient(
                        0, 0,
                        0.5, 0.5,
                        1.0,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0.55, Color.TRANSPARENT),
                        new Stop(1.0, Color.color(0, 0, 0, 0.78))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        vignetteLayer.setMouseTransparent(true);
        vignetteLayer.setOpacity(0.7);

        noiseCanvas.setMouseTransparent(true);
        noiseCanvas.setOpacity(0.18);

        particleLayer.setMouseTransparent(true);
        particleLayer.setEffect(new GaussianBlur(6));
        createDriftingParticles(26);
    }

    /**
     * Creates an internal container that holds all background layers and adds it
     * as the bottom-most child of the target StackPane.
     */
    private void attachLayers() {
        StackPane container = new StackPane();
        container.setPickOnBounds(false);
        container.getChildren().addAll(gradientLayer, noiseCanvas, particleLayer, vignetteLayer);
        target.getChildren().add(0, container);
    }

    /**
     * Binds all layers to the target's size so the background scales automatically
     * with window resizes, and triggers noise regeneration on size changes.
     */
    private void bindSizes() {
        gradientLayer.prefWidthProperty().bind(target.widthProperty());
        gradientLayer.prefHeightProperty().bind(target.heightProperty());

        vignetteLayer.prefWidthProperty().bind(target.widthProperty());
        vignetteLayer.prefHeightProperty().bind(target.heightProperty());

        noiseCanvas.widthProperty().bind(target.widthProperty());
        noiseCanvas.heightProperty().bind(target.heightProperty());

        // regenerate noise texture whenever the target is resized
        target.widthProperty().addListener((obs, oldVal, newVal) -> refreshNoise());
        target.heightProperty().addListener((obs, oldVal, newVal) -> refreshNoise());
    }

    private void refreshNoise() {
        double width = target.getWidth();
        double height = target.getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        GraphicsContext gc = noiseCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int particles = (int) Math.max(80, Math.min(160, (width * height) / 6000));

        for (int i = 0; i < particles; i++) {
            double x = random.nextDouble(width);
            double y = random.nextDouble(height);
            double radius = random.nextDouble(0.6, 1.8);
            double opacity = random.nextDouble(0.05, 0.12);
            gc.setFill(Color.color(0.8, 0.9, 1.0, opacity));
            gc.fillOval(x, y, radius, radius);
        }
    }

    private void createDriftingParticles(int count) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < count; i++) {
            Circle particle = new Circle();
            particle.setRadius(random.nextDouble(1.0, 2.8));
            particle.setFill(Color.color(1.0, 1.0, 1.0, 0.42));
            particle.setCenterX(0);
            particle.setCenterY(0);

            double normalizedX = random.nextDouble();
            double normalizedY = random.nextDouble();

            particle.layoutXProperty().bind(target.widthProperty().multiply(normalizedX));
            particle.layoutYProperty().bind(target.heightProperty().multiply(normalizedY));

            double driftX = random.nextDouble(-70, 70);
            double driftY = random.nextDouble(85, 150);

            Timeline drift = new Timeline(
                    // start: no translation, curretn opacity
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(particle.translateXProperty(), 0),
                            new KeyValue(particle.translateYProperty(), 0),
                            new KeyValue(particle.opacityProperty(), particle.getOpacity())
                    ),

                    //end: translated by driftX/driftY and slightly adjusted opacity
                    new KeyFrame(Duration.seconds(random.nextDouble(12, 22)),
                            new KeyValue(particle.translateXProperty(), driftX),
                            new KeyValue(particle.translateYProperty(), driftY),
                            new KeyValue(particle.opacityProperty(), random.nextDouble(0.3, 0.55))
                    )
            );
            drift.setAutoReverse(true);
            drift.setCycleCount(Animation.INDEFINITE);

            particleLayer.getChildren().add(particle);
            particleTimelines.add(drift);
        }
    }

    private void startAnimations() {

        // slow pulse on the vignette opacity for a breathing effect
        shimmerTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(vignetteLayer.opacityProperty(), 0.65)),
                new KeyFrame(Duration.seconds(7), new KeyValue(vignetteLayer.opacityProperty(), 0.78))
        );
        shimmerTimeline.setAutoReverse(true);
        shimmerTimeline.setCycleCount(Animation.INDEFINITE);
        shimmerTimeline.play();

        // start all particle drift timelines
        particleTimelines.forEach(Timeline::play);
    }
}


