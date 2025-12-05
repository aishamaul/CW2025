package com.comp2042.ui.view.menu;

import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.view.SceneNavigator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private Label titleLabel;

    private final MenuButtonAnimator menuButtonAnimator = new MenuButtonAnimator();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundAnimator.attach(rootPane);
        menuButtonAnimator.animateButtons(rootPane);

    }

    @FXML
    public void onPlayClicked(ActionEvent event) throws IOException {
        SceneNavigator.switchTo("playMenu.fxml", event);
    }

    @FXML
    public void onControlsClicked(ActionEvent event) throws IOException {
        SceneNavigator.switchTo("gameControls.fxml", event);
    }

    @FXML
    public void onHighScoreClicked(ActionEvent event) {
        System.out.println("High Score clicked - Not implemented yet");
    }

    @FXML
    public void onQuitClicked(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
