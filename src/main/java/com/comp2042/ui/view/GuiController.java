package com.comp2042.ui.view;

import com.comp2042.game.core.GameProgressionManager;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.ui.components.PauseButtonAnimator;
import com.comp2042.ui.input.EventDispatcher;
import com.comp2042.ui.input.InputController;
import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
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

/**
 * The primary JavaFX Controller class for the game view.
 * <p>
 *     This class implements the {@link GameView} interface and serves as the bridge between
 *     the FXML UI definition and the game logic core. It initializes the game components,
 *     sets up event listeners for user input, and manages high level UI states like
 *     menus, game-over screens, and level transitions.
 * </p>
 */

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

    private final ControlsMenuLoader controlsMenuLoader = new ControlsMenuLoader();

    private GameViewEventDelegate eventDelegate;

    /**
     * Called by JavaFX when the FXML file is loaded.
     * Sets up the initial UI state, attaches animators, and initializes sound.
     */
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


    /**
     * Sets the game more before the game starts.
     * @param mode The GameMode strategy to use.
     */
    public void setGameMode(GameMode mode) {
        this.currentGameMode = mode;
        if (eventDelegate != null)eventDelegate.setCurrentGameMode(mode);
        if (dispatcher != null) dispatcher.setGameMode(mode);
        if (flowCoordinator != null) flowCoordinator.setCurrentMode(mode);
    }

    /**
     * Enables or disables Classic Mode (standard rules).
     * @param isClassic true for Classic Mode, false for other modes.
     */
    public void setClassicMode(boolean isClassic) {
        this.isClassicMode = isClassic;
    }

    /**
     * Initializes the game view with data from the core logic
     * This setup includes creating the runtime manager, flow coordinator and input bindings.
     *
     * @param boardMatrix The initial 2D array of the board.
     * @param brick The initial view data for the active brick.
     */
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

        this.eventDelegate = new GameViewEventDelegate(
                uiManager, animationCoordinator, flowCoordinator,
                gameOverMenu, runtimeManager.isGameOverProperty(), linesLabel
        );
        this.eventDelegate.setCurrentGameMode(currentGameMode);

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


    /**
     * Updates the visual representation of the active brick.
     * Skipped if the game is paused or currently animating a line clear.
     */
    @Override
    public void refreshBrick(ViewData brick) {
        if (runtimeManager.isPauseProperty().getValue() == Boolean.FALSE || animationCoordinator.isAnimating()) {            uiManager.refresh(brick);
        }
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        uiManager.refreshBackground(board);
    }

    /**
     * Callback method triggered by the game loop or user input to move the brick down.
     */
    public void moveDown(EventType eventType, EventSource source) {
        DownData downData = dispatcher.moveDown(eventType, source);
        refreshBrick(downData.getViewData());
        rootPane.requestFocus();
    }

    /**
     * Sets the event listener that will handle game logic events triggered by the UI.
     * @param eventListener The listener
     */
    @Override
    public void setEventListener(InputEventListener eventListener) {
        this.dispatcher = new EventDispatcher(eventListener);
    }

    /**
     * Binds the UI score label to the core logic's score property.
     */
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
        eventDelegate.gameOver();
    }

    @Override
    public void showScoreNotification(String text) {
        eventDelegate.showScoreNotification(text);
    }

    @Override
    public void onLineClear(List<Integer> lines, Runnable onAnimationFinished){
        eventDelegate.onLineClear(lines, onAnimationFinished);
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
        controlsMenuLoader.showGameControls(rootPane, pauseMenu, pauseButton);
    }

    @Override
    public void onExplosion(List<java.awt.Point> explodedPoints, Runnable onAnimationFinished) {
        eventDelegate.onExplosion(explodedPoints, onAnimationFinished);
    }

    @Override
    public void onBrickLanded(){
        eventDelegate.onBrickLanded();
    }

    @Override
    public void  onHardDrop(ViewData brick){
        eventDelegate.onHardDrop(brick);
    }


}
