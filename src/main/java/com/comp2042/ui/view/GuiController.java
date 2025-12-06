package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.core.GameProgressionManager;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.ui.components.PauseButtonAnimator;
import com.comp2042.ui.input.EventDispatcher;
import com.comp2042.ui.input.InputController;
import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.view.menu.GameControlsController;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.menu.LevelMenuController;


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

    private GameUIManager uiManager;

    private PauseStateManager pauseStateManager;

    private GameRuntimeManager runtimeManager;

    private GameProgressionManager progressionManager; // New Dependency

    private boolean isClassicMode = false;

    private GameMode currentGameMode;

    private FreezeOverlayManager freezeOverlayManager;

    private InputController inputController = new InputController();

    private final LevelStartManager levelStartManager = new LevelStartManager();

    private GameFlowCoordinator flowCoordinator;

    private AnimationCoordinator animationCoordinator;

    private PauseButtonAnimator pauseButtonAnimator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundAnimator.attach(rootPane);
        List<GridPane> nextBrickGrids = Arrays.asList(nextBrick1, nextBrick2, nextBrick3);

        this.uiManager = new GameUIManager(gameBoard, gamePanel, brickPanel, ghostPanel, nextBrickGrids, holdBrickGrid, groupNotification, scoreLabel);

        this.freezeOverlayManager = new FreezeOverlayManager(timerLabel, overlayMessageLabel);

        WindowScaler.bindScaling(rootPane, contentPane);

        setupPauseButtonPulse();

        if (pauseMenu != null) pauseMenu.setVisible(false);

        SoundManager.getInstance().attachButtonSounds(rootPane);
    }

    private void setupPauseButtonPulse() {
        pauseButtonAnimator = new PauseButtonAnimator(pauseButton);
        pauseButtonAnimator.start();
    }


    public void setGameMode(GameMode mode) {
        this.currentGameMode = mode;
        if (dispatcher != null) dispatcher.setGameMode(mode);
        if (flowCoordinator != null) flowCoordinator.setCurrentMode(mode);
    }


    public void setClassicMode(boolean isClassic) {
        this.isClassicMode = isClassic;
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        uiManager.initGameView(boardMatrix, brick);
        rootPane.setFocusTraversable(true);
        rootPane.requestLayout();

        this.runtimeManager = new GameRuntimeManager(
                dispatcher,
                freezeOverlayManager,
                this::moveDown,
                () -> currentGameMode
        );

        this.progressionManager = new GameProgressionManager(
                runtimeManager, () -> currentGameMode, isClassicMode
        );

        this.flowCoordinator = new GameFlowCoordinator(
                runtimeManager.getGameLoopManager(), levelMenusController,
                runtimeManager.isPauseProperty(), runtimeManager.isGameOverProperty(),
                this::newGame, this::setGameMode
        );
        this.flowCoordinator.setCurrentMode(currentGameMode);

        this.animationCoordinator = new AnimationCoordinator(
                runtimeManager.getGameLoopManager(), runtimeManager.isPauseProperty(),
                (visible) -> {
                    if (brickPanel != null) brickPanel.setVisible(visible);
                    if (ghostPanel != null) ghostPanel.setVisible(visible);
                }
        );

        this.pauseStateManager = new PauseStateManager(runtimeManager.getGameLoopManager(), pauseButton, runtimeManager.isPauseProperty(), pauseMenu);

        inputController.bindInputs(rootPane, this, dispatcher,
                runtimeManager.isPauseProperty(), runtimeManager.isGameOverProperty(),
                this::moveDown, this::newGame, this::togglePauseMenu);

        levelStartManager.registerCallbacks(levelMenusController,
                flowCoordinator::startCurrentLevel,
                flowCoordinator::startNextLevel,
                () -> flowCoordinator.navigateToHomeWithNode(rootPane));

        if (currentGameMode != null){
            showLevelStartScreen();
        } else{
            runtimeManager.startLoop();
        }
    }

    private void showLevelStartScreen() {
        runtimeManager.setPause(true);
        String desc = (currentGameMode != null) ? currentGameMode.getDescription() : "";
        levelStartManager.showStartScreen(levelMenusController, currentGameMode.getName(), desc);
    }


    @Override
    public void refreshBrick(ViewData brick) {
        if (runtimeManager.isPauseProperty().getValue() == Boolean.FALSE || animationCoordinator.isAnimating()) {            uiManager.refresh(brick);
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
            if (progressionManager != null) {
                progressionManager.onLinesUpdated(newValue.intValue());
            }
        });
    }

    @Override
    public void gameOver() {
        SoundManager.getInstance().playLoseSound();
        flowCoordinator.handleGameOver();

        gameOverMenu.setVisible(true);
        gameOverMenu.toFront();

        runtimeManager.setGameOver(true);
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
        if (runtimeManager != null) runtimeManager.stopLoop();

        if (brickPanel != null) brickPanel.setVisible(true);
        if (ghostPanel != null) ghostPanel.setVisible(true);

        gameOverMenu.setVisible(false);
        if(levelMenusController != null) levelMenusController.hideAll();

        dispatcher.newGame();
        rootPane.requestFocus();

        runtimeManager.resetState();
        pauseStateManager.reset();

        if (currentGameMode != null) {
            showLevelStartScreen();
        } else {
            runtimeManager.startLoop();
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

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameControls.fxml"));
            Parent controlsRoot = loader.load();

            GameControlsController controller = loader.getController();

            controller.setOnBackAction(() -> {
                rootPane.getChildren().remove(controlsRoot);

                pauseMenu.setVisible(true);
                pauseMenu.toFront();

                if (pauseButton != null) {
                    rootPane.requestFocus();
                }
            });

            pauseMenu.setVisible(false);

            rootPane.getChildren().add(controlsRoot);
            } catch (IOException e) {
            e.printStackTrace();
        }

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
