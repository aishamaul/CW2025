package com.comp2042.ui.view.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LevelMenuController {

    @FXML private VBox levelStartMenu;

    @FXML private Label levelTitleLabel;

    @FXML private VBox levelCompleteMenu;

    @FXML private Button nextLevelButton;

    private Runnable onStartAction;
    private Runnable onNextLevelAction;
    private Runnable onExitAction;

    public void  setCallbacks(Runnable onStart, Runnable onNextLevel, Runnable onExit){
        this.onStartAction = onStart;
        this.onNextLevelAction = onNextLevel;
        this.onExitAction = onExit;
    }

    public void showStartScreen(String levelName){
        hideAll();
        levelTitleLabel.setText(levelName);
        levelStartMenu.setVisible(true);
        levelStartMenu.toFront();
    }

    public void showLevelComplete(boolean hasNextLevel) {
        hideAll();
        levelCompleteMenu.setVisible(true);
        levelCompleteMenu.toFront();

        if (!hasNextLevel) {
            nextLevelButton.setVisible(false);
            nextLevelButton.setManaged(false);
        } else {
            nextLevelButton.setVisible(true);
            nextLevelButton.setManaged(true);
            nextLevelButton.setText("NEXT LEVEL");
        }
    }

    public void hideAll() {
        levelStartMenu.setVisible(false);
        levelCompleteMenu.setVisible(false);
    }

    @FXML
    private void onStartClicked() {
        if (onStartAction != null) onStartAction.run();
    }

    @FXML
    private void onNextLevelClicked() {
        if (onNextLevelAction != null) onNextLevelAction.run();
    }

    @FXML
    private void onExitClicked() {
        if (onExitAction != null) onExitAction.run();
    }

}
