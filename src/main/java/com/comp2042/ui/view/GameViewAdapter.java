package com.comp2042.ui.view;

import com.comp2042.model.ViewData;
import javafx.beans.property.IntegerProperty;

/**
 * Adapter class that implements {@link GameView} and delegates calls to a wrapped instance.
 * <p>
 * This class allows for simpler interaction from the game core by providing
 * helper methods before passing data to the raw view.
 * </p>
 */
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
