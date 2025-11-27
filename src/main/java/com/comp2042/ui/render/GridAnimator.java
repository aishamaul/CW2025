package com.comp2042.ui.render;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class GridAnimator {

    public void animateClear (Rectangle[][] displayMatrix, List<Integer> clearIndices, Runnable onFinished){
        ParallelTransition parallelTransition = new ParallelTransition();
        Random random = new Random();

        for (Integer rowIndex : clearIndices) {
            if (rowIndex < 0 || rowIndex >= displayMatrix.length) continue;

            for (int col = 0; col < displayMatrix[rowIndex].length; col++) {
                Rectangle rect = displayMatrix[rowIndex][col];
                if (rect == null) continue;

                //flash
                FillTransition flash = new FillTransition(Duration.millis(50), rect, (Color) rect.getFill(), Color.WHITE);

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

        parallelTransition.setOnFinished(e->{
            resetAnimatedRectangles(displayMatrix, clearIndices);
            onFinished.run();
        });
        parallelTransition.play();
    }

    private void resetAnimatedRectangles(Rectangle[][] displayMatrix, List<Integer> clearIndices){
        for (Integer rowIndex : clearIndices) {
            for (int col = 0; col < displayMatrix[rowIndex].length; col++) {
                Rectangle rect = displayMatrix[rowIndex][col];
                if (rect != null){
                    rect.setOpacity(1.0);
                    rect.setTranslateX(0);
                    rect.setTranslateY(0);
                    rect.setScaleX(1.0);
                    rect.setScaleY(1.0);
                }
            }
        }
    }

}

