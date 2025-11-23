package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

public class PauseStateManager {

    private final GameLoopManager gameLoopManager;
    private final ToggleButton pauseButton;
    private final BooleanProperty isPause;

    public PauseStateManager(GameLoopManager gameLoopManager, ToggleButton pauseButton, BooleanProperty isPause) {
        this.gameLoopManager = gameLoopManager;
        this.pauseButton = pauseButton;
        this.isPause = isPause;

    }

    public void  togglePause() {
        if (pauseButton.isSelected()) {
            isPause.setValue(Boolean.TRUE);
            gameLoopManager.pause();

            pauseButton.getStyleClass().remove("pauseButton");
            if(!pauseButton.getStyleClass().contains("playButton")) {
                pauseButton.getStyleClass().add("playButton");
            }
        }else{
            isPause.setValue(Boolean.FALSE);
            gameLoopManager.play();

            pauseButton.getStyleClass().remove("playButton");
            if(!pauseButton.getStyleClass().contains("pauseButton")) {
                pauseButton.getStyleClass().add("pauseButton");
            }
        }
    }

    public void reset(){
        isPause.setValue(Boolean.FALSE);
        pauseButton.setDisable(false);
        pauseButton.setSelected(false);
        pauseButton.getStyleClass().remove("playButton");
        if(!pauseButton.getStyleClass().contains("pauseButton")) {
            pauseButton.getStyleClass().add("pauseButton");
        }
    }
}
