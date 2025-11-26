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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable, GameView {

    @FXML
    private StackPane rootPane;

    @FXML
    private Pane contentPane;

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
    private Label scoreLabel;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private VBox nextBrickContainer;

    @FXML
    private GridPane nextBrick1;
    @FXML
    private GridPane nextBrick2;
    @FXML
    private GridPane nextBrick3;

    @FXML
    private GridPane holdBrickGrid;

    @FXML
    private VBox pauseMenu;

    @FXML
    private VBox gameOverMenu;

    @FXML
    private Label linesLabel;

    private EventDispatcher dispatcher;

    private GameLoopManager gameLoopManager;

    private GameUIManager uiManager;

    private PauseStateManager pauseStateManager;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private boolean isClearingLines = false;

    private boolean isClassicMode = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<GridPane> nextBrickGrids = Arrays.asList(nextBrick1, nextBrick2, nextBrick3);

        this.uiManager = new GameUIManager(gameBoard, gamePanel, brickPanel, ghostPanel, nextBrickGrids, holdBrickGrid, groupNotification, scoreLabel);

        WindowScaler.bindScaling(rootPane, contentPane);

        if(pauseMenu!=null)pauseMenu.setVisible(false);
    }

    public void setClassicMode(boolean isClassic) {
        this.isClassicMode = isClassic;
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        uiManager.initGameView(boardMatrix, brick);

        rootPane.setFocusTraversable(true);
        rootPane.requestLayout();
        rootPane.setOnKeyPressed(new InputHandler(
                this,
                dispatcher,
                isPause,
                isGameOver,
                this::moveDown,
                this:: newGame,
                this::togglePauseMenu));

        gameLoopManager = new GameLoopManager(() -> moveDown(EventType.DOWN, EventSource.THREAD));
        this.pauseStateManager = new PauseStateManager(gameLoopManager, pauseButton, isPause, pauseMenu);
        gameLoopManager.play();


    }



    @Override
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE ||  isClearingLines) {
            uiManager.refresh(brick);
        }
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        uiManager.refreshBackground(board);
    }

    public void moveDown(EventType eventType, EventSource source) {
        DownData downData = dispatcher.moveDown(eventType, source);
        refreshBrick(downData.getViewData());
        rootPane.requestFocus();
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
    public void bindLines(IntegerProperty integerProperty) {
        linesLabel.textProperty().bind(integerProperty.asString());

        // add listener to update speed if in classic mode
        integerProperty.addListener((observable, oldValue, newValue) -> {
            if (isClassicMode && gameLoopManager != null) {
                int lines = newValue.intValue();
                //increase speed every 10 lines
                double newRate = 1.0 + (lines / 5) * 0.5;
                gameLoopManager.setRate(newRate);

            }
        });
    }

    @Override
    public void gameOver() {
        gameLoopManager.stop();

        gameOverMenu.setVisible(true);
        gameOverMenu.toFront();

        isGameOver.setValue(Boolean.TRUE);
    }

    @Override
    public void showScoreNotification(String text) {
        uiManager.showNotification(text);
    }

    @Override
    public void onLineClear(List<Integer> lines, Runnable onAnimationFinished){
        isClearingLines = true;
        isPause.setValue(Boolean.TRUE);
        gameLoopManager.pause();

        if (brickPanel != null) brickPanel.setVisible(false);
        if (ghostPanel != null) ghostPanel.setVisible(false);

        uiManager.animateClear(lines, ()->{
            isClearingLines = false;
            isPause.setValue(Boolean.FALSE); // Unblock input

            if (brickPanel != null) brickPanel.setVisible(true);
            if (ghostPanel != null) ghostPanel.setVisible(true);

            onAnimationFinished.run();

            gameLoopManager.play();
        });
    }

    public void newGame() {
        gameLoopManager.stop();

        isClearingLines = false;
        if (brickPanel != null) brickPanel.setVisible(true);
        if (ghostPanel != null) ghostPanel.setVisible(true);

        gameOverMenu.setVisible(false);

        dispatcher.newGame();
        rootPane.requestFocus();
        gameLoopManager.play();
        isGameOver.setValue(Boolean.FALSE);

        pauseStateManager.reset();
    }

    private void togglePauseMenu() {
        if (isClearingLines) return;
        pauseStateManager.togglePause();
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        pauseStateManager.togglePause();
        rootPane.requestFocus();

    }

    @FXML
    public void resumeGame(ActionEvent actionEvent) {
        // return to game button
        pauseStateManager.hidePauseMenu();
        rootPane.requestFocus();
    }

    @FXML
    public void restartGame(ActionEvent actionEvent) {
        // restart button
        newGame();
    }

    @FXML
    public void goToHome(ActionEvent actionEvent){
        //stop the game loop before leaving
        if (gameLoopManager != null){
            gameLoopManager.stop();
        }
        try{
            //load  the  home screen
            Parent homeRoot = FXMLLoader.load(getClass().getClassLoader().getResource("home.fxml"));
            Scene homeScene = new Scene(homeRoot);

            //get stage from the event source
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(homeScene);
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    public void showGameControls(ActionEvent actionEvent) {
        //go to show game controls screen (not implemented yet)
    }


}
