package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.menu.LevelMenuController;
import com.comp2042.util.audio.SoundManager;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Orchestrates the flow of the game, including level transitions, game over states, and navigation.
 * <p>
 * This class serves as the central logic for moving between screens
 * and managing the global state flags associated with those transitions.
 * </p>
 */
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

    /**
     * Sets the current active game mode.
     * @param mode The GameMode to track.
     */
    public void setCurrentMode(GameMode mode) {
        this.currentMode = mode;
    }

    /**
     * Starts the gameplay for the current level.
     * <p>
     * Hides menus and unpauses the game loop.
     * </p>
     */
    public void startCurrentLevel() {
        if (levelMenuController != null) {
            levelMenuController.hideAll();
        }
        isPause.setValue(false);
        gameLoopManager.play();
    }

    /**
     * Advances the game to the next level defined by the current mode.
     * <p>
     * If no next level exists, it navigates back to the home screen.
     * </p>
     */
    public void startNextLevel() {
        if (currentMode != null && currentMode.getNextLevel() != null) {
            gameModeSetter.accept(currentMode.getNextLevel());
            newGameCallback.run(); // triggers the reset in main controller
        } else {
            navigateToHome(null);
        }
    }

    /**
     * Handles the logic when a level is completed.
     * <p>
     * Stops the loop, plays sound, and shows the appropriate win screen.
     * </p>
     */
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

    /**
     * Handles the Game Over state.
     * <p>
     * Stops the game loop and sets the game over flag.
     * </p>
     */
    public void handleGameOver() {
        gameLoopManager.stop();
        isGameOver.setValue(true);
    }

    /**
     * Navigates back to the main menu (Home).
     * @param event The ActionEvent triggering the navigation (can be null).
     */
    public void navigateToHome(ActionEvent event) {
        gameLoopManager.stop();
        try {
            SceneNavigator.switchTo("home.fxml", event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Programmatic navigation to home using a Node context instead of an event.
     * @param context The JavaFX node used to locate the Stage.
     */    public void navigateToHomeWithNode(javafx.scene.Node context) {
        gameLoopManager.stop();
        try {
            SceneNavigator.switchTo("home.fxml", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
