package com.comp2042;

import com.comp2042.game.core.GameController;
import com.comp2042.game.core.GameInitializer;
import com.comp2042.ui.view.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 400, 700);
        primaryStage.setScene(scene);
        // Enable Resizing
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(350);
        primaryStage.setMinHeight(650);
        primaryStage.show();
        new GameInitializer(c);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
