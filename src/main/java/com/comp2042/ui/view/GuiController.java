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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import javafx.scene.transform.Scale;


import java.net.URL;
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
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane ghostPanel;

    private EventDispatcher dispatcher;

    private GameLoopManager gameLoopManager;

    private GameUIManager uiManager;

    private PauseStateManager pauseStateManager;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private static final double DESIGN_WIDTH = 400.0;
    private static final double DESIGN_HEIGHT = 700.0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.uiManager = new GameUIManager(gameBoard, gamePanel, brickPanel, ghostPanel,  groupNotification, gameOverPanel, scoreLabel);

        setupScaling();
    }

    private void setupScaling() {
        //create a scale transform
        Scale scale = new Scale(1,1);
        scale.setPivotX(0);
        scale.setPivotY(0);
        contentPane.getTransforms().add(scale);

        //Listener to handle window resizing
        Runnable resizeHandler = () -> {
            double windowWidth = rootPane.getWidth();
            double windowHeight = rootPane.getHeight();

            //calculate the scale factor to fit the window while maintaining the aspect ratio
            double scaleFactor = Math.min(
                    windowHeight/DESIGN_HEIGHT,
                    windowWidth/DESIGN_WIDTH
            );

            //apply the scale
            scale.setX(scaleFactor);
            scale.setY(scaleFactor);
        };
        // bind the listener to the root pane's dimensions
        rootPane.widthProperty().addListener((obs,oldVal, newVal)-> resizeHandler.run());
        rootPane.heightProperty().addListener((obs,oldVal, newVal)-> resizeHandler.run());

        //run once to set the initial state
        resizeHandler.run();


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
                () -> pauseGame(null)));

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
    public void gameOver() {
        gameLoopManager.stop();
        uiManager.showGameOver();
        isGameOver.setValue(Boolean.TRUE);
    }

    @Override
    public void showScoreNotification(String text) {
        uiManager.showNotification(text);
    }

    public void newGame() {
        gameLoopManager.stop();
        uiManager.hideGameOver();
        dispatcher.newGame();
        rootPane.requestFocus();
        gameLoopManager.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        pauseStateManager.reset();
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        if(actionEvent==null){
            pauseButton.setSelected(!pauseButton.isSelected());
        }

        pauseStateManager.togglePause();
        rootPane.requestFocus();

    }
}
