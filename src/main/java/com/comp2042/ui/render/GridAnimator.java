package com.comp2042.ui.render;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Manages complex animations for the game grid.
 * <p>
 * This class handles the logic for row clearing flashes, explosion particle effects,
 * and board shake animations.
 * </p>
 */
public class GridAnimator {

    private final BrickStyler brickStyler = new BrickStyler();
    private final Random random = new Random();

    /**
     * Animates the removal of full rows.
     * <p>
     * Applies flash, move, shrink, and fade transitions to the blocks in the cleared rows.
     * </p>
     * @param displayMatrix The matrix of Rectangles representing the board.
     * @param clearIndices  The row indices to animate.
     * @param onFinished    Callback to run after the animation sequence.
     */
    public void animateClear(Rectangle[][] displayMatrix, List<Integer> clearIndices, Runnable onFinished) {
        ParallelTransition parallelTransition = new ParallelTransition();

        for (Integer rowIndex : clearIndices) {
            if (rowIndex < 0 || rowIndex >= displayMatrix.length) continue;

            for (int col = 0; col < displayMatrix[rowIndex].length; col++) {
                Rectangle rect = displayMatrix[rowIndex][col];
                if (rect == null) continue;

                // flash
                Color startColor = rect.getFill() instanceof Color ? (Color) rect.getFill() : Color.WHITE;
                FillTransition flash = new FillTransition(Duration.millis(50), rect, startColor, Color.WHITE);

                // move: calculate randomness inline
                TranslateTransition move = new TranslateTransition(Duration.millis(200), rect);
                move.setByX((random.nextDouble() - 0.5) * 600);
                move.setByY((random.nextDouble() - 0.5) * 600);

                // shrink: use constructor for duration and node
                ScaleTransition shrink = new ScaleTransition(Duration.millis(200), rect);
                shrink.setToX(0.1);
                shrink.setToY(0.1);

                FadeTransition fade = new FadeTransition(Duration.millis(220), rect);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);

                SequentialTransition fullSeq = new SequentialTransition(flash, new ParallelTransition(move, shrink, fade));
                parallelTransition.getChildren().add(fullSeq);
            }
        }

        parallelTransition.setOnFinished(e -> {
            resetAnimatedRectangles(displayMatrix, clearIndices);
            onFinished.run();
        });
        parallelTransition.play();
    }

    /**
     * Animates an explosion effect for specific blocks when the bomb lands.
     * <p>
     * Causes blocks to flash and collapse, while spawning particle debris radiating
     * from the explosion center.
     * </p>
     * @param displayMatrix  The matrix of rectangles representing the board.
     * @param explodedPoints The coordinates of the blocks to explode.
     * @param onFinished     Callback to run after the animation sequence.
     */
    public void animateExplosion(Rectangle[][] displayMatrix, List<Point> explodedPoints, Runnable onFinished) {
        ParallelTransition parallel = new ParallelTransition();

        // bomb center is the middle of the block list provided
        Point center = explodedPoints.get(0);
        double cx = center.x;
        double cy = center.y;

        for (Point p : explodedPoints) {
            int r = p.y;
            int c = p.x;

            if (r < 0 || r >= displayMatrix.length || c < 0 || c >= displayMatrix[0].length) continue;

            Rectangle rect = displayMatrix[r][c];
            if (rect == null) continue;

            performExplosionOnBlock(parallel, rect, cx, cy, c, r);
        }

        parallel.setOnFinished(e -> {
            // reset the exploded bricks
            for (Point p : explodedPoints) {
                if (p.y >= 0 && p.y < displayMatrix.length && p.x >= 0 && p.x < displayMatrix[0].length) {
                    Rectangle rect = displayMatrix[p.y][p.x];
                    if (rect != null) resetRectangle(rect);
                }
            }
            onFinished.run();
        });

        parallel.play();
    }

    /**
     * Handles the animation for a single block in the explosion:
     * Flashes and shrinks the original block.
     * Spawns chaotic particles.
     */
    private void performExplosionOnBlock(ParallelTransition parentTransition,
                                         Rectangle rect,
                                         double centerX, double centerY,
                                         int col, int row) {

        // instant flash
        Color startColor = extractColor(rect.getFill());
        FillTransition flash = new FillTransition(Duration.millis(20), rect, startColor, Color.WHITE);

        // calculate outward vector
        double dx = col - centerX;
        double dy = row - centerY;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len != 0) { dx /= len; dy /= len; }

        // instant collapse
        ScaleTransition shrink = new ScaleTransition(Duration.millis(100), rect);
        shrink.setToX(0.0);
        shrink.setToY(0.0);

        FadeTransition fade = new FadeTransition(Duration.millis(100), rect);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ParallelTransition blockCollapse = new ParallelTransition(shrink, fade);
        SequentialTransition blockSequence = new SequentialTransition(flash, blockCollapse);

        parentTransition.getChildren().add(blockSequence);

        // spawn particles
        if (rect.getParent() instanceof Pane) {
            Pane parentPane = (Pane) rect.getParent();
            spawnExplosionParticles(parentPane, rect, dx, dy);
        }
    }

    /**
     * Generates multiple tiny particles that fly outward from the block's position.
     */
    private void spawnExplosionParticles(Pane parentPane, Rectangle sourceRect, double dirX, double dirY) {
        javafx.geometry.Bounds bounds = sourceRect.getBoundsInParent();
        double originX = bounds.getMinX() + (bounds.getWidth() / 2);
        double originY = bounds.getMinY() + (bounds.getHeight() / 2);

        // increased particle count
        int particleCount = 8;
        Paint sourceFill = sourceRect.getFill() != null ? sourceRect.getFill() : Color.WHITE;

        for (int i = 0; i < particleCount; i++) {

            // varied sizes
            double pSize = 3 + random.nextDouble() * 8;
            Rectangle particle = new Rectangle(pSize, pSize);
            particle.setFill(sourceFill);

            particle.setTranslateX(originX);
            particle.setTranslateY(originY);

            parentPane.getChildren().add(particle);

            double spread = 1.5;
            double randX = dirX + (random.nextDouble() - 0.5) * spread;
            double randY = dirY + (random.nextDouble() - 0.5) * spread;

            double distance = 100 + random.nextDouble() * 150;

            double durationMillis = 150 + random.nextDouble() * 200;

            ParticleAnimatorHelper.animateDriftAndFade(
                    particle,
                    parentPane,
                    Duration.millis(durationMillis),
                    randX * distance,
                    randY * distance,
                    Duration.millis(50),
                    Interpolator.EASE_OUT
            );
        }
    }

    private void resetAnimatedRectangles(Rectangle[][] displayMatrix, List<Integer> clearIndices) {
        for (Integer rowIndex : clearIndices) {
            for (int col = 0; col < displayMatrix[rowIndex].length; col++) {
                Rectangle rect = displayMatrix[rowIndex][col];
                if (rect != null) resetRectangle(rect);
            }
        }
    }

    private void resetRectangle(Rectangle rect) {
        rect.setOpacity(1.0);
        rect.setTranslateX(0);
        rect.setTranslateY(0);
        rect.setScaleX(1.0);
        rect.setScaleY(1.0);
    }

    public void animateBoardShake (Node boardNode){
        //push down quickly
        TranslateTransition down = new TranslateTransition(Duration.millis(50), boardNode);
        down.setByY(5.0);
        down.setInterpolator(Interpolator.EASE_OUT);

        //return to normal (bounce back)
        TranslateTransition up = new TranslateTransition(Duration.millis(120), boardNode);
        up.setByY(-5.0);
        up.setInterpolator(Interpolator.EASE_IN);

        SequentialTransition shake = new SequentialTransition(down, up);
        shake.play();


    }

    public void spawnSplashParticles(Pane parentPane, double x, double y, int colorCode) {
        int particleCount = 2;
        for (int i=0; i < particleCount; i++){
            double size = 2+ random.nextDouble() * 4;
            Rectangle p = new Rectangle(size, size);
            brickStyler.applyBrickStyle(p, colorCode);

            p.setTranslateX(x + 10);
            p.setTranslateY(y + 20);

            parentPane.getChildren().add(p);

            double angle = 270 + (random.nextDouble() - 0.5) * 60;
            double velocity = 50 + random.nextDouble() * 100;

            double rad = Math.toRadians(angle);
            double moveX = Math.cos(rad) * velocity;
            double moveY = Math.sin(rad) * velocity;

            // animation
            double moveDuration = 300 + random.nextDouble() * 200;

            ParticleAnimatorHelper.animateDriftAndFade(
                    p,
                    parentPane,
                    Duration.millis(moveDuration),
                    moveX,
                    moveY,
                    Duration.ZERO,
                    Interpolator.EASE_OUT
            );
        }
    }

    private Color extractColor (Paint paint){
        if (paint instanceof Color color){
            return color;
        }
        if (paint instanceof LinearGradient gradient && !gradient.getStops().isEmpty()){
            return gradient.getStops().get(0).getColor();
        }
        return Color.WHITE;
    }
}