package com.comp2042.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.IOException;

public class SceneNavigator {
    /**
     * Loads an FXML file and switches the current stage to it
     */

    public static void switchTo(String fxmlPath, ActionEvent triggerEvent) throws IOException {
        Parent root = FXMLLoader.load(SceneNavigator.class.getClassLoader().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) triggerEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
