package com.comp2042.ui.view;

import com.comp2042.model.ViewData;
import com.comp2042.ui.components.GameOverPanel;
import com.comp2042.ui.components.NotificationManager;
import com.comp2042.ui.render.GameRenderer;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.awt.*;

public class GameUIManager {

    private final GameRenderer gameRenderer;
    private final NotificationManager notificationManager;
    private final GameOverPanel gameOverPanel;
    private final Label scoreLabel;
    private final GridPane gamePanel;

    public GameUIManager(BorderPane gameBoard, GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel, Group groupNotification, GameOverPanel gameOverPanel, Label scoreLabel) {
        this.gamePanel = gamePanel;
        this.gameOverPanel = gameOverPanel;
        this.scoreLabel = scoreLabel;


        this.notificationManager = new NotificationManager(groupNotification);
        this.gameRenderer = new GameRenderer(gameBoard, gamePanel, brickPanel, ghostPanel);
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
        gameOverPanel.setVisible(false);

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

    public void showGameOver(){
        gameOverPanel.setVisible(true);
    }

    public void hideGameOver(){
        gameOverPanel.setVisible(false);
    }

    public void bindScore(IntegerProperty scoreProperty){
        scoreLabel.textProperty().bind(scoreProperty.asString());
    }

    public void showNotification(String text){
        notificationManager.showScore(text);
    }

    public void requestFocus(){
        gamePanel.requestFocus();
    }


}
