package com.comp2042.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneNavigator {
    /**
     * Loads an FXML file and switches the current stage to it
     */

    public static void switchTo(String fxmlPath, ActionEvent triggerEvent) throws IOException {
        Node source = (Node) triggerEvent.getSource();
        switchTo(fxmlPath, source);
    }

    /**
     * Overload: Switches stage using a context Node (e.g. rootPane)
     * Useful when ActionEvent is not available (callbacks)
     */
    public static void switchTo(String fxmlPath, Node contextNode) throws IOException {
        Parent root = FXMLLoader.load(SceneNavigator.class.getClassLoader().getResource(fxmlPath));
        Stage stage = (Stage) contextNode.getScene().getWindow();

        Scene scene = stage.getScene();
        if (scene != null) {
            scene.setRoot(root);
        } else {
            stage.setScene(new Scene(root));
        }
        stage.setFullScreen(true);
        stage.show();
    }

}
