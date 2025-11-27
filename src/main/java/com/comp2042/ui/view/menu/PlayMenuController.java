package com.comp2042.ui.view.menu;

import com.comp2042.game.core.GameInitializer;
import com.comp2042.game.mode.ChallengeLevel1;
import com.comp2042.ui.view.GuiController;
import com.comp2042.ui.view.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayMenuController {

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
        Scene gameScene = new Scene(gameRoot);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // ensure the game window focuses so key inputs work immediately
        gameRoot.requestFocus();

        stage.setScene(gameScene);
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
