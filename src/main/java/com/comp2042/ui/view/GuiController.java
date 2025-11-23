package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.ui.components.GameOverPanel;
import com.comp2042.ui.components.NotificationManager;
import com.comp2042.ui.input.EventDispatcher;
import com.comp2042.ui.input.InputHandler;
import com.comp2042.ui.render.GameRenderer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, GameView {

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private BorderPane gameBoard;

    @FXML
    private ToggleButton pauseButton;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel;

    private EventDispatcher dispatcher;

    private GameLoopManager gameLoopManager;

    private GameUIManager uiManager;

    private PauseStateManager pauseStateManager;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.uiManager = new GameUIManager(gameBoard, gamePanel, brickPanel, groupNotification, gameOverPanel, scoreLabel);
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        uiManager.initGameView(boardMatrix, brick);

        gamePanel.setOnKeyPressed(new InputHandler(this, dispatcher, isPause, isGameOver, this::moveDown, () -> newGame(null), () -> pauseGame(null)));

        gameLoopManager = new GameLoopManager(() -> moveDown(EventType.DOWN, EventSource.THREAD));
        gameLoopManager.play();

        this.pauseStateManager = new PauseStateManager(gameLoopManager, pauseButton, isPause);
    }



    @Override
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            uiManager.refresh(brick);
        }
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        uiManager.refreshBackground(board);
    }

    public void moveDown(EventType eventType, EventSource source) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = dispatcher.moveDown(eventType, source);
            refreshBrick(downData.getViewData());
        }
        uiManager.requestFocus();
    }

    @Override
    public void setEventListener(InputEventListener eventListener) {
        this.dispatcher = new EventDispatcher(eventListener);
    }

    @Override
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    @Override
    public void gameOver() {
        gameLoopManager.stop();
        uiManager.showGameOver();
        isGameOver.setValue(Boolean.TRUE);
    }

    @Override
    public void showScoreNotification(String text) {
        uiManager.showNotification(text);
    }

    public void newGame(ActionEvent actionEvent) {
        gameLoopManager.stop();
        uiManager.hideGameOver();
        dispatcher.newGame();
        uiManager.requestFocus();
        gameLoopManager.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        pauseButton.setDisable(false);
        pauseButton.setSelected(false);
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        if(actionEvent==null){
            pauseButton.setSelected(!pauseButton.isSelected());
        }

        pauseStateManager.togglePause(actionEvent);
        uiManager.requestFocus();

    }
}
