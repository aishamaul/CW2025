package com.comp2042.ui.view.menu;

import com.comp2042.game.core.GameInitializer;
import com.comp2042.game.mode.ChallengeLevel1;
import com.comp2042.game.mode.ChallengeLevel3;
import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.view.GuiController;
import com.comp2042.ui.view.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayMenuController implements Initializable {

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundAnimator.attach(rootPane);
    }

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

    @FXML
    public void onPracticeClicked(ActionEvent event) throws IOException {
        launchGame(event, false, false);
    }

    @FXML
    public void onClassicClicked(ActionEvent event) throws IOException {
        launchGame(event, true, false);
    }

    @FXML
    public void onChallengeClicked(ActionEvent event) throws IOException {
        // Launch in Challenge Mode
        launchGame(event, false, true);
    }


    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        SceneNavigator.switchTo("playMenu.fxml", event);
    }
}
