package com.comp2042.ui.view.menu;

import com.comp2042.game.core.GameInitializer;
import com.comp2042.game.mode.ChallengeLevel1;
import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.view.GuiController;
import com.comp2042.ui.view.SceneNavigator;
import com.comp2042.util.audio.SoundManager;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the play mode selection menu.
 * <p>
 *     Handles the selection of practice, classic or challenge modes and launches
 *     the game scene with the appropriate configuration.
 * </p>
 */
public class PlayMenuController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private Label titleLabel;

    private final MenuButtonAnimator menuButtonAnimator = new MenuButtonAnimator();

    /**
     * Initializes the play menu.
     * @param location  Relative path location.
     * @param resources Resources bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundAnimator.attach(rootPane);
        animateMenuElements();
        SoundManager.getInstance().attachButtonSounds(rootPane);
    }

    private void animateMenuElements() {
        menuButtonAnimator.animateButtons(rootPane);
        animateTitleLabel();
    }

    private void animateTitleLabel() {
        if (titleLabel == null) {
            return;
        }

        DropShadow glow = new DropShadow();
        glow.setRadius(26);
        glow.setSpread(0.45);
        glow.setColor(Color.web("#7af3ff"));
        titleLabel.setEffect(glow);

        Timeline colorLoop = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.colorProperty(), Color.web("#ff85d8"))),
                new KeyFrame(Duration.seconds(1.4), new KeyValue(glow.colorProperty(), Color.web("#7af3ff"))),
                new KeyFrame(Duration.seconds(2.6), new KeyValue(glow.colorProperty(), Color.web("#ffe38f"))),
                new KeyFrame(Duration.seconds(3.8), new KeyValue(glow.colorProperty(), Color.web("#c0a9ff")))
        );
        colorLoop.setCycleCount(Animation.INDEFINITE);
        colorLoop.setAutoReverse(true);
        colorLoop.play();

        FadeTransition breathe = new FadeTransition(Duration.seconds(2.2), titleLabel);
        breathe.setFromValue(0.78);
        breathe.setToValue(1.0);
        breathe.setCycleCount(Animation.INDEFINITE);
        breathe.setAutoReverse(true);
        breathe.play();
    }


    /**
     * Common logic to load and launch the game scene.
     * @param event The triggering event.
     * @param isClassic True if Classic rules should apply.
     * @param isChallenge True if Challenge mode is selected (overrides classic).
     * @throws IOException If FXML loading fails.
     */
    @FXML
    public void launchGame(ActionEvent event, boolean  isClassic, boolean isChallenge) throws IOException {
        // load the game layout
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
        Parent gameRoot = loader.load();

        // initialize the game
        GuiController controller = loader.getController();

        if (isChallenge) {
            // Set the mode to Challenge Level 1
            controller.setGameMode(new ChallengeLevel1());
        } else {
            // Classic or Practice
            controller.setClassicMode(isClassic);
        }
        new GameInitializer(controller);

        //switch to the game scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene gameScene = stage.getScene();
        if (gameScene != null) {
            gameScene.setRoot(gameRoot);
        } else {
            gameScene = new Scene(gameRoot);
            stage.setScene(gameScene);
        }

        // ensure the game window focuses so key inputs work immediately
        gameRoot.requestFocus();

        stage.setFullScreen(true);
        stage.show();

    }

    /**
     * Handler for "Practice" button. Launches game with no special rules.
     * @param event Click event.
     * @throws IOException If FXML fails.
     */
    @FXML
    public void onPracticeClicked(ActionEvent event) throws IOException {
        launchGame(event, false, false);
    }

    /**
     * Handler for "Classic" button. Launches game with speed progression.
     * @param event Click event.
     * @throws IOException If FXML fails.
     */
    @FXML
    public void onClassicClicked(ActionEvent event) throws IOException {
        launchGame(event, true, false);
    }

    /**
     * Handler for "Challenge" button. Launches game in Challenge Mode (Level 1).
     * @param event Click event.
     * @throws IOException If FXML fails.
     */
    @FXML
    public void onChallengeClicked(ActionEvent event) throws IOException {
        // Launch in Challenge Mode
        launchGame(event, false, true);
    }


    /**
     * Navigates back to the Home screen.
     * @param event Click event.
     * @throws IOException If FXML fails.
     */
    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        SceneNavigator.switchTo("home.fxml", event);
    }
}
