package com.comp2042.ui.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    public void onPlayClicked(ActionEvent event) throws IOException {
        // load the play menu
        Parent playMenuRoot = FXMLLoader.load(getClass().getClassLoader().getResource("playMenu.fxml"));
        Scene playMenuScene = new  Scene(playMenuRoot);

        // get the current stage and set the new scene
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(playMenuScene);
        stage.show();
    }

    @FXML
    public void onControlsClicked(ActionEvent event) {
        System.out.println("game controls not implemented yet");
    }

    @FXML
    public void onHighScoreClicked(ActionEvent event) {
        System.out.println("High Score clicked - Not implemented yet");
    }
}
