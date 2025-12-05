package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.menu.LevelMenuController;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.function.Consumer;

public class GameFlowCoordinator {

    private final GameLoopManager gameLoopManager;
    private final LevelMenuController levelMenuController;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;
    private final Runnable newGameCallback;
    private final Consumer<GameMode> gameModeSetter;
    private GameMode currentMode;

    public GameFlowCoordinator(GameLoopManager gameLoopManager,
                               LevelMenuController levelMenuController,
                               BooleanProperty isPause,
                               BooleanProperty isGameOver,
                               Runnable newGameCallback,
                               Consumer<GameMode> gameModeSetter) {
        this.gameLoopManager = gameLoopManager;
        this.levelMenuController = levelMenuController;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
        this.newGameCallback = newGameCallback;
        this.gameModeSetter = gameModeSetter;
    }

    public void setCurrentMode(GameMode mode) {
        this.currentMode = mode;
    }

    public void startCurrentLevel() {
        if (levelMenuController != null) {
            levelMenuController.hideAll();
        }
        isPause.setValue(false);
        gameLoopManager.play();
    }


    public void startNextLevel() {
        if (currentMode != null && currentMode.getNextLevel() != null) {
            gameModeSetter.accept(currentMode.getNextLevel());
            newGameCallback.run(); // triggers the reset in main controller
        } else {
            navigateToHome(null);
        }
    }

    public void handleLevelComplete(){
        SoundManager.getInstance().playWinSound();
        gameLoopManager.stop();
        boolean hasNext = (currentMode != null && currentMode.getNextLevel() != null);
        if (levelMenuController != null) {
            if (hasNext) {
                // show standard level complete screen
                levelMenuController.showLevelComplete(true);
            } else {
                // last level finished, challenge complete
                levelMenuController.showChallengeComplete();
            }        }
    }

    public void handleGameOver() {
        gameLoopManager.stop();
        isGameOver.setValue(true);
    }

    public void navigateToHome(ActionEvent event) {
        gameLoopManager.stop();
        try {
            SceneNavigator.switchTo("home.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New Method: Handles programmatic navigation using a Node
    public void navigateToHomeWithNode(javafx.scene.Node context) {
        gameLoopManager.stop();
        try {
            SceneNavigator.switchTo("home.fxml", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
