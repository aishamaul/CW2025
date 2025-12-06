package com.comp2042.ui.view;

import com.comp2042.game.mode.GameMode;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;

public class GameViewEventDelegate {
    private final GameUIManager uiManager;
    private final AnimationCoordinator animationCoordinator;
    private final GameFlowCoordinator flowCoordinator;
    private final VBox gameOverMenu;
    private final BooleanProperty isGameOver;
    private final Label linesLabel;

    private GameMode currentGameMode;

    public GameViewEventDelegate(GameUIManager uiManager,
                                 AnimationCoordinator animationCoordinator,
                                 GameFlowCoordinator flowCoordinator,
                                 VBox gameOverMenu,
                                 BooleanProperty isGameOver,
                                 Label linesLabel) {
        this.uiManager = uiManager;
        this.animationCoordinator = animationCoordinator;
        this.flowCoordinator = flowCoordinator;
        this.gameOverMenu = gameOverMenu;
        this.isGameOver = isGameOver;
        this.linesLabel = linesLabel;
    }

    public void setCurrentGameMode(GameMode mode) {
        this.currentGameMode = mode;
    }

    public void gameOver() {
        SoundManager.getInstance().playLoseSound();
        flowCoordinator.handleGameOver();

        if (gameOverMenu != null) {
            gameOverMenu.setVisible(true);
            gameOverMenu.toFront();
        }

        isGameOver.setValue(Boolean.TRUE);
    }

    public void showScoreNotification(String text) {
        uiManager.showNotification(text);
    }

    public void onLineClear(List<Integer> lines, Runnable onAnimationFinished) {
        Runnable finisher = animationCoordinator.createFinishes(onAnimationFinished,
                () -> {
                    int currentLines = 0;
                    try {
                        currentLines = Integer.parseInt(linesLabel.getText());
                    } catch (NumberFormatException ignored) {}

                    if (currentGameMode != null && currentGameMode.isWinConditionMet(currentLines)) {
                        flowCoordinator.handleLevelComplete();
                        return true;
                    }
                    return false;
                });
        animationCoordinator.runAnimationFlow(
                () -> uiManager.animateClear(lines, finisher)
        );
    }

    public void onExplosion(List<Point> explodedPoints, Runnable onAnimationFinished) {
        Runnable finisher = animationCoordinator.createFinishes(onAnimationFinished, () -> false);

        animationCoordinator.runAnimationFlow(
                () -> uiManager.animateExplosion(explodedPoints, finisher)
        );
    }

    public void onBrickLanded() {
        uiManager.animateLanding();
    }

    public void onHardDrop(com.comp2042.model.ViewData brick) {
        uiManager.animateHardDrop(brick);
    }

}
