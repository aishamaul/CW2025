package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Manages the logic for toggling the pause state and showing/hiding the pause menu.
 * <p>
 * This class ensures that the game loop, UI visibility, and fullscreen state
 * remain synchronized when the user pauses or resumes the game.
 * </p>
 */
public class PauseStateManager {

    private final GameLoopManager gameLoopManager;
    private final ToggleButton pauseButton;
    private final BooleanProperty isPause;
    private final VBox pauseMenu;

    public PauseStateManager(GameLoopManager gameLoopManager, ToggleButton pauseButton, BooleanProperty isPause, VBox pauseMenu) {
        this.gameLoopManager = gameLoopManager;
        this.pauseButton = pauseButton;
        this.isPause = isPause;
        this.pauseMenu = pauseMenu;

    }

    /**
     * Toggles the current pause state.
     */
    public void togglePause() {
        if (isPause.get()) {
            hidePauseMenu();
        } else{
            showPauseMenu();
            }
        }


    /**
     * Pauses the game and shows the menu.
     */
        public void showPauseMenu() {
        isPause.setValue(Boolean.TRUE);
        gameLoopManager.pause();

        pauseMenu.setVisible(true);
        pauseMenu.toFront();

        if (!pauseButton.isSelected()) {
            pauseButton.setSelected(true);
        }

        setFullScreen(true);
    }

    /**
     * Resumes the game and hides the menu.
     */
    public void hidePauseMenu(){
        isPause.setValue(Boolean.FALSE);
        gameLoopManager.play();

        pauseMenu.setVisible(false);

        if (pauseButton.isSelected()) {
            pauseButton.setSelected(false);
        }

        setFullScreen(true);
        }

    /**
     * Resets the pause state to unpaused (used for new games).
     */
        public void reset(){
        isPause.setValue(Boolean.FALSE);
        pauseButton.setDisable(false);
        pauseButton.setSelected(false);
        pauseMenu.setVisible(false);

        setFullScreen(true);
    }

    private void setFullScreen(boolean fullScreen) {
        if (pauseButton.getScene() != null && pauseButton.getScene().getWindow() instanceof Stage) {
            Stage stage = (Stage) pauseButton.getScene().getWindow();
            stage.setFullScreen(fullScreen);

            if (fullScreen) {
                stage.show(); // Helps refresh layout on some OS
                pauseButton.getScene().getRoot().requestFocus();
            }
        }
    }
}
