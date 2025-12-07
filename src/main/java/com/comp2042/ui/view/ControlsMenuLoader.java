package com.comp2042.ui.view;

import com.comp2042.ui.view.menu.GameControlsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Helper class responsible for loading and displaying the game controls overlay menu.
 * <p>
 * It handles loading the FXML, injecting dependencies, and managing the
 * back-navigation logic to return to the pause menu.
 * </p>
 */
public class ControlsMenuLoader {

    public void showGameControls(StackPane rootPane, VBox pauseMenu, ToggleButton pauseButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameControls.fxml"));
            Parent controlsRoot = loader.load();

            GameControlsController controller = loader.getController();

            controller.setOnBackAction(() -> {
                rootPane.getChildren().remove(controlsRoot);

                if (pauseMenu != null) {
                    pauseMenu.setVisible(true);
                    pauseMenu.toFront();
                }

                if (pauseButton != null) {
                    rootPane.requestFocus();
                }
            });

            if (pauseMenu != null) {
                pauseMenu.setVisible(false);
            }

            rootPane.getChildren().add(controlsRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
