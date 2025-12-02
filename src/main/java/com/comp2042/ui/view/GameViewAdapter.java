package com.comp2042.ui.view;

import com.comp2042.model.ViewData;
import javafx.beans.property.IntegerProperty;

public class GameViewAdapter {

    public final GameView view;

    public GameViewAdapter(GameView view) {
        this.view = view;
    }

    public void initializeView(int[][] boardMatrix, ViewData brick) {
        this.view.initGameView(boardMatrix, brick);
    }

    public void bindScore(IntegerProperty scoreProperty) {
        view.bindScore(scoreProperty);
    }

    public void bindLines(IntegerProperty linesProperty) { view.bindLines(linesProperty); }

    public void refreshGameBackground(int[][] board) {
        view.refreshGameBackground(board);
    }

    public void refreshBrick(ViewData brick) {
        view.refreshBrick(brick);
    }

    public void gameOver() {
        view.gameOver();
    }

    public void showScoreNotification(int scoreBonus) {
        view.showScoreNotification("+" + scoreBonus);
    }

    public void onLineClear(java.util.List<Integer> lines, Runnable onAnimationFinished) {
        view.onLineClear(lines, onAnimationFinished);
    }

    public void onExplosion(java.util.List<java.awt.Point> explodedPoints, Runnable onAnimationFinished) {
        view.onExplosion(explodedPoints, onAnimationFinished);
    }

    public void onBrickLanded(){
        view.onBrickLanded();
    }

    public void onHardDrop(ViewData brick){
        view.onHardDrop(brick);
    }
}
