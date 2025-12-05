package com.comp2042.ui.view.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LevelMenuController {

    @FXML private VBox levelStartMenu;

    @FXML private Label levelTitleLabel;

    @FXML private VBox levelCompleteMenu;

    @FXML private Button nextLevelButton;

    @FXML private StackPane rootStack;

    @FXML private Pane effectsPane;

    @FXML private Label levelDescriptionLabel;

    @FXML private VBox challengeCompleteMenu;

    private Runnable onStartAction;
    private Runnable onNextLevelAction;
    private Runnable onExitAction;

    private final ChallengeMenuAnimator animator = new ChallengeMenuAnimator();

    public void  setCallbacks(Runnable onStart, Runnable onNextLevel, Runnable onExit){
        this.onStartAction = onStart;
        this.onNextLevelAction = onNextLevel;
        this.onExitAction = onExit;
    }

    public void showStartScreen(String levelName, String description){
        hideAll();
        levelTitleLabel.setText(levelName);
        levelDescriptionLabel.setText(description);
        levelStartMenu.setVisible(true);
        levelStartMenu.toFront();
    }

    public void showLevelComplete(boolean hasNextLevel) {
        hideAll();
        levelCompleteMenu.setVisible(true);
        levelCompleteMenu.toFront();

        if (effectsPane != null) {
            animator.playFallingBricks(effectsPane);
        }

        if (!hasNextLevel) {
            nextLevelButton.setVisible(false);
            nextLevelButton.setManaged(false);
        } else {
            nextLevelButton.setVisible(true);
            nextLevelButton.setManaged(true);
        }
    }

    public void showChallengeComplete() {
        hideAll();
        challengeCompleteMenu.setVisible(true);
        challengeCompleteMenu.toFront();

        if (effectsPane != null) {
            animator.playConfetti(effectsPane);
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
