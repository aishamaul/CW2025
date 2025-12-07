package com.comp2042.ui.view;

import com.comp2042.model.ViewData;
import com.comp2042.ui.components.NotificationManager;
import com.comp2042.ui.render.GameRenderer;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import java.util.List;

import java.awt.*;

/**
 * Manages the high-level UI components of the game view.
 * <p>
 * This class aggregates the {@link GameRenderer} (for the grid) and the
 * {@link NotificationManager} (for popups), providing a unified interface for
 * updating the visual state of the game.
 * </p>
 */
public class GameUIManager {

    private final GameRenderer gameRenderer;
    private final NotificationManager notificationManager;
    private final Label scoreLabel;
    private final GridPane gamePanel;

    /**
     * Constructs a new GameUIManager.
     * @param gameBoard         The root layout pane.
     * @param gamePanel         The grid for the board.
     * @param brickPanel        The grid for the active brick.
     * @param ghostPanel        The grid for the ghost piece.
     * @param nextBrickGrids    The grids for next piece previews.
     * @param holdBrickGrid     The grid for the held piece.
     * @param groupNotification The container for notifications.
     * @param scoreLabel        The label displaying the score.
     */
    public GameUIManager(BorderPane gameBoard, GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel,
                         List<GridPane> nextBrickGrids, GridPane holdBrickGrid,
                         Group groupNotification, Label scoreLabel) {
        this.gamePanel = gamePanel;
        this.scoreLabel = scoreLabel;


        this.notificationManager = new NotificationManager(groupNotification);
        this.gameRenderer = new GameRenderer(gameBoard, gamePanel, brickPanel, ghostPanel, nextBrickGrids, holdBrickGrid);
        setupVisuals();
    }

    private void setupVisuals() {
        try{
            Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        } catch (Exception e){
            System.err.println("Could not load font: " + e.getMessage());
        }

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        gameRenderer.initGameView(boardMatrix, brick);
    }

    public void refresh(ViewData brick) {
        gameRenderer.refreshBrick(brick);
    }

    public void refreshBackground(int[][] board) {
        gameRenderer.refreshGameBackground(board);
    }

    public void showNotification(String text){
        notificationManager.showScore(text);
    }

    public void animateClear(List<Integer> lines, Runnable onFinished){
        gameRenderer.animateClear(lines, onFinished);
    }

    public void animateExplosion(List<java.awt.Point> explodedPoints, Runnable onFinished){
        gameRenderer.animateExplosion(explodedPoints, onFinished);
    }

    public void animateLanding(){
        gameRenderer.playLandingAnimation();
    }

    public void animateHardDrop(ViewData brick){
        gameRenderer.playHardDropParticleEffect(brick);
    }

}
