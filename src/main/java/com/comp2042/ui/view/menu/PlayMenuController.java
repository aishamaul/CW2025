package com.comp2042.ui.view.menu;

import com.comp2042.game.core.GameInitializer;
import com.comp2042.ui.view.GuiController;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayMenuController {

    @FXML
    public void onPracticeClicked(ActionEvent event) throws IOException {
        // load the game layout
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
        Parent gameRoot = loader.load();

        // initialize the game
        GuiController controller = loader.getController();
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
    public void onBackClicked(ActionEvent event) throws IOException {
        // return to home screen
        Parent homeRoot = FXMLLoader.load(getClass().getClassLoader().getResource("home.fxml"));
        Scene homeScene = new Scene(homeRoot);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(homeScene);
        stage.show();
    }
}
