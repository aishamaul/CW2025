package com.comp2042.ui.view;

import com.comp2042.game.events.InputEventListener;
import com.comp2042.model.ViewData;
import javafx.beans.property.IntegerProperty;

import java.util.List;

public interface GameView {

    void initGameView(int [][] boardMatrix, ViewData brick);

    void refreshGameBackground(int [][] board);

    void refreshBrick(ViewData brick);

    void gameOver();

    void bindScore(IntegerProperty scoreProperty);

    void bindLines(IntegerProperty linesProperty);

    void setEventListener(InputEventListener eventListener);

    void showScoreNotification(String text);

    void onLineClear(List<Integer> lines, Runnable onAnimationFinished);

    void onExplosion(List<java.awt.Point> explodedPoints, Runnable onAnimationFinished);

    void onBrickLanded();
}
