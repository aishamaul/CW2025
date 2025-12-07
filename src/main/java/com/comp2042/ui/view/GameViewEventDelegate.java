package com.comp2042.ui.view;

import com.comp2042.game.mode.GameMode;
import com.comp2042.model.ViewData;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;

/**
 * Handles the logic for complex UI events like animations, game over sequences, and level completion checks.
 * <p>
 * This class offloads the heavy lifting from the main {@link GuiController}, serving as a
 * dedicated handler for visual events that require coordination between animations,
 * flow control, and game state updates.
 * </p>
 */
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

    /**
     * Updates the current game mode.
     * @param mode The active GameMode.
     */
    public void setCurrentGameMode(GameMode mode) {
        this.currentGameMode = mode;
    }

    /**
     * Executes the Game Over sequence.
     * <p>
     * Plays sound, stops flow, shows menu, and sets state flags.
     * </p>
     */
    public void gameOver() {
        SoundManager.getInstance().playLoseSound();
        flowCoordinator.handleGameOver();

        if (gameOverMenu != null) {
            gameOverMenu.setVisible(true);
            gameOverMenu.toFront();
        }

        isGameOver.setValue(Boolean.TRUE);
    }

    /**
     * Shows a score notification.
     * @param text The text to display.
     */
    public void showScoreNotification(String text) {
        uiManager.showNotification(text);
    }

    /**
     * Handles the line clear event.
     * <p>
     *     Runs the animation and then checks if the level win condition has been met.
     * </p>
     * @param lines The cleared lines.
     * @param onAnimationFinished Callback provided by the core logic.
     */
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

    /**
     * Handles the explosion event.
     * @param explodedPoints The exploded points.
     * @param onAnimationFinished Callback provided by the core logic.
     */
    public void onExplosion(List<Point> explodedPoints, Runnable onAnimationFinished) {
        Runnable finisher = animationCoordinator.createFinishes(onAnimationFinished, () -> false);

        animationCoordinator.runAnimationFlow(
                () -> uiManager.animateExplosion(explodedPoints, finisher)
        );
    }

    /**
     * Triggers the landing animation.
     */
    public void onBrickLanded() {
        uiManager.animateLanding();
    }

    /**
     * Triggers the hard drop animation
     * @param brick The brick data.
     */
    public void onHardDrop(ViewData brick) {
        uiManager.animateHardDrop(brick);
    }

}
