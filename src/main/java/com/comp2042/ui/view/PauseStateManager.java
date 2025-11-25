package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

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

    public void  togglePause() {
        if (isPause.get()) {
            hidePauseMenu();
        } else{
            showPauseMenu();
            }
        }


    public void showPauseMenu() {
        isPause.setValue(Boolean.TRUE);
        gameLoopManager.pause();

        pauseMenu.setVisible(true);
        pauseMenu.toFront();

        if (!pauseButton.isSelected()) {
            pauseButton.setSelected(true);
        }
    }

    public void hidePauseMenu(){
        isPause.setValue(Boolean.FALSE);
        gameLoopManager.play();

        pauseMenu.setVisible(false);

        if (pauseButton.isSelected()) {
            pauseButton.setSelected(false);
        }

        }

    public void reset(){
        isPause.setValue(Boolean.FALSE);
        pauseButton.setDisable(false);
        pauseButton.setSelected(false);
        pauseMenu.setVisible(false);
    }
}
