package com.comp2042.ui.view.menu;

import com.comp2042.ui.render.BackgroundAnimator;
import com.comp2042.ui.view.SceneNavigator;
import com.comp2042.util.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the game controls help screen.
 * <p>
 *     Displays a list of key bindings and actions to the user. It supports being shown
 *     either as a standalone screen or as an overlay within the game scene.
 * </p>
 */
public class GameControlsController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox controlsContainer;

    private Runnable onBackAction;

    /**
     * Initializes the controller class.
     * Generates the control rows dynamically
     * @param location The location used to resolve relative paths.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // attach the existing animated background
        BackgroundAnimator.attach(rootPane);

        // clear placeholder content
        controlsContainer.getChildren().clear();

        // controls based on InputHandler logic
        // colors cycle through the brick IDs
        addControlRow("A / ←", "MOVE LEFT", 1);
        addControlRow("D / →", "MOVE RIGHT", 2);
        addControlRow("W / ↑", "ROTATE", 3);
        addControlRow("S / ↓", "SOFT DROP", 4);
        addControlRow("SPACE", "HARD DROP", 5);
        addControlRow("H", "HOLD BRICK", 6);
        addControlRow("ESC", "PAUSE / RESUME", 7);
        addControlRow("N", "NEW GAME", 9);

        SoundManager.getInstance().attachButtonSounds(rootPane);
    }

    /**
     * Sets a custom action to be executed when "Back" is clicked.
     * <p>
     *     Used when the controls are shown as an overlay (to remove the overlay)
     *     rather than navigating to a new scene.
     *</p>
     * @param onBackAction The runnable to execute.
     */
    public void setOnBackAction(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }



    private void addControlRow(String keys, String action, int colorCode) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);

        // convert the int colorCode to a web string for CSS usage
        String colorHex = getHexColor(colorCode);

        // left panel (keys)
        StackPane keyPanel = createStyledPanel(keys, colorHex, 180);

        // arrow indicator
        Label arrow = new Label("▶");
        arrow.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-opacity: 0.8;");

        // right panel (actions)
        StackPane actionPanel = createStyledPanel(action, colorHex, 250);

        row.getChildren().addAll(keyPanel, arrow, actionPanel);
        controlsContainer.getChildren().add(row);
    }

    private StackPane createStyledPanel(String text, String colorHex, double width) {
        StackPane panel = new StackPane();
        panel.setPrefWidth(width);
        panel.setPrefHeight(45);
        panel.getStyleClass().add("controlPanel"); // See CSS below

        // apply color glow and border
        String style = String.format(
                "-fx-border-color: %s;" +
                        "-fx-background-color: transparent;" +
                        "-fx-effect: dropshadow(gaussian, %s, 15, 0.4, 0, 0);",
                colorHex, colorHex
        );
        panel.setStyle(style);

        // background (semitransparent version of the color)
        Rectangle bg = new Rectangle(width, 45);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        bg.setFill(Color.TRANSPARENT);        bg.setStroke(Color.TRANSPARENT);

        Label label = new Label(text);
        label.getStyleClass().add("controlText");
        label.setStyle("-fx-text-fill: white");

        panel.getChildren().addAll(bg, label);
        return panel;
    }

    /**
     * Handles the back button click.
     * @param event The action event.
     * @throws IOException If navigation fails (when using SceneNavigator fallback).
     */
    @FXML
    public void onBackClicked(ActionEvent event) throws IOException {
        if (onBackAction != null) {
            onBackAction.run();
        } else {
            SceneNavigator.switchTo("home.fxml", event);
        }    }

    private String getHexColor(int i) {
        return switch (i) {
            case 1 -> "#FFA500";
            case 2 -> "#FF4500";
            case 3 -> "#FFFF00";
            case 4 -> "#ADFF2F";
            case 5 -> "#00FFFF";
            case 6 -> "#8A2BE2";
            case 7 -> "#FF1493";
            case 8 -> "#808080";
            case 9 -> "#FF0000";
            default -> "#FFFFFF";
        };
    }


}
