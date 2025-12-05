package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.ui.components.GameOverPanel;
import com.comp2042.ui.components.NotificationManager;
import com.comp2042.ui.input.EventDispatcher;
import com.comp2042.ui.input.InputController;
import com.comp2042.ui.input.InputHandler;
import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.render.GameRenderer;
import javafx.animation.FadeTransition;
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
import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.menu.LevelMenuController;
import javafx.util.Duration;


import java.io.IOException;
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

    @FXML
    private LevelMenuController levelMenusController;

    @FXML
    private Label overlayMessageLabel;

    @FXML
    private Label timerLabel;

    private EventDispatcher dispatcher;

    private GameLoopManager gameLoopManager;

    private GameUIManager uiManager;

    private PauseStateManager pauseStateManager;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    private boolean isClassicMode = false;

    private GameMode currentGameMode;

    private FreezeOverlayManager freezeOverlayManager;

    private InputController inputController = new InputController();

    private final LevelStartManager levelStartManager = new LevelStartManager();

    private GameFlowCoordinator flowCoordinator;

    private AnimationCoordinator animationCoordinator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundAnimator.attach(rootPane);
        List<GridPane> nextBrickGrids = Arrays.asList(nextBrick1, nextBrick2, nextBrick3);

        this.uiManager = new GameUIManager(gameBoard, gamePanel, brickPanel, ghostPanel, nextBrickGrids, holdBrickGrid, groupNotification, scoreLabel);

        this.freezeOverlayManager = new FreezeOverlayManager(timerLabel, overlayMessageLabel);

        WindowScaler.bindScaling(rootPane, contentPane);


        if (pauseMenu != null) pauseMenu.setVisible(false);
    }

    public void setGameMode(GameMode mode) {
        this.currentGameMode = mode;

        if (dispatcher != null) {
            dispatcher.setGameMode(mode);
        }

        if (flowCoordinator != null) {
            flowCoordinator.setCurrentMode(mode);
        }
    }


    public void setClassicMode(boolean isClassic) {
        this.isClassicMode = isClassic;
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        uiManager.initGameView(boardMatrix, brick);
        rootPane.setFocusTraversable(true);
        rootPane.requestLayout();

        gameLoopManager = new GameLoopManager(() -> {
            boolean applyGravity = dispatcher.onGameTick(currentGameMode);
            if (applyGravity) {
                moveDown(EventType.DOWN, EventSource.THREAD);
            }
            freezeOverlayManager.updateOverlay(currentGameMode);
        });

        this.flowCoordinator = new GameFlowCoordinator(
                gameLoopManager, levelMenusController, isPause, isGameOver,
                this::newGame, this::setGameMode
        );
        this.flowCoordinator.setCurrentMode(currentGameMode);

        this.animationCoordinator = new AnimationCoordinator(
                gameLoopManager, isPause,
                (visible) -> {
                    if (brickPanel != null) brickPanel.setVisible(visible);
                    if (ghostPanel != null) ghostPanel.setVisible(visible);
                }
        );

        this.pauseStateManager = new PauseStateManager(gameLoopManager, pauseButton, isPause, pauseMenu);

        inputController.bindInputs(rootPane, this, dispatcher, isPause, isGameOver,
                this::moveDown, this::newGame, this::togglePauseMenu);

        levelStartManager.registerCallbacks(levelMenusController,
                flowCoordinator::startCurrentLevel,
                flowCoordinator::startNextLevel,
                () -> flowCoordinator.navigateToHome(null));

        if (currentGameMode != null){
            showLevelStartScreen();
        } else{
            gameLoopManager.play();
        }
    }

    private void showFreezeNotification() {
        overlayMessageLabel.setOpacity(1.0);
        overlayMessageLabel.setVisible(true);
        overlayMessageLabel.toFront();

        FadeTransition fade = new FadeTransition(Duration.seconds(3), overlayMessageLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(0.5));
        fade.setOnFinished(e -> overlayMessageLabel.setVisible(false));
        fade.play();
    }


    private void showLevelStartScreen() {
        isPause.setValue(true);
        levelStartManager.showStartScreen(levelMenusController, currentGameMode.getName());
    }


    @Override
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE || animationCoordinator.isAnimating()) {
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

        integerProperty.addListener((observable, oldValue, newValue) -> {
            int lines = newValue.intValue();

            if (currentGameMode != null && gameLoopManager != null) {
                currentGameMode.onLinesUpdated(lines, gameLoopManager);
            }
            else if (isClassicMode && gameLoopManager != null) {
                double newRate = 1.0 + (lines/5) * 0.5;
                gameLoopManager.setRate(newRate);
            }
        });
    }

    @Override
    public void gameOver() {
        flowCoordinator.handleGameOver();

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
        Runnable finisher = animationCoordinator.createFinishes(onAnimationFinished,
                ()->{
                        int currentLines = Integer.parseInt(linesLabel.getText());
                        if(currentGameMode != null && currentGameMode.isWinConditionMet(currentLines)){
                            flowCoordinator.handleLevelComplete();
                            return true;
                        }
                        return false;
                });
        animationCoordinator.runAnimationFlow(
                () -> uiManager.animateClear(lines, finisher)
        );
    }

    public void newGame() {
        if (gameLoopManager != null) gameLoopManager.stop();

        if (brickPanel != null) brickPanel.setVisible(true);
        if (ghostPanel != null) ghostPanel.setVisible(true);

        gameOverMenu.setVisible(false);
        if(levelMenusController != null) levelMenusController.hideAll();

        dispatcher.newGame();
        rootPane.requestFocus();

        gameLoopManager.setRate(1.0);
        pauseStateManager.reset();
        isGameOver.setValue(Boolean.FALSE);

        if (currentGameMode != null) {
            showLevelStartScreen(); // This sets isPause to true
        } else {
            gameLoopManager.play();
        }
    }

    private void togglePauseMenu() {
        if (animationCoordinator != null && animationCoordinator.isAnimating()) return;
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
        flowCoordinator.navigateToHome(actionEvent);
    }


    @FXML
    public void showGameControls(ActionEvent actionEvent) {
        //go to show game controls screen (not implemented yet)
    }

    @Override
    public void onExplosion(List<java.awt.Point> explodedPoints, Runnable onAnimationFinished) {
        Runnable finisher = animationCoordinator.createFinishes(onAnimationFinished, ()-> false);

        animationCoordinator.runAnimationFlow(
                () -> uiManager.animateExplosion(explodedPoints, finisher)
        );
    }

    @Override
    public void onBrickLanded(){
        uiManager.animateLanding();
    }

    @Override
    public void  onHardDrop(ViewData brick){
        uiManager.animateHardDrop(brick);
    }


}
