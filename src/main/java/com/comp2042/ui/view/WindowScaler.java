package com.comp2042.ui.view;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

public class WindowScaler {
    private static final double DESIGN_WIDTH = 400.0;
    private static final double DESIGN_HEIGHT = 700.0;

    public static void bindScaling(StackPane rootPane, Pane contentPane){
        //create a scale transform
        Scale scale = new Scale(1, 1);
        scale.setPivotX(0);
        scale.setPivotY(0);
        contentPane.getTransforms().add(scale);

        //listener to handle window resizing
        Runnable resizeHandler = () -> {
            double windowWidth = rootPane.getWidth();
            double windowHeight = rootPane.getHeight();

            //calculate the scale factor to fit the window while maintaining the aspect ratio
            double scaleFactor = Math.min(windowHeight/DESIGN_HEIGHT, windowWidth/DESIGN_WIDTH);

            //apply the scale
            scale.setX(scaleFactor);
            scale.setY(scaleFactor);
        };

        // bind the listener to the root pane's dimensions
        rootPane.widthProperty().addListener((obs, o, n)-> resizeHandler.run());
        rootPane.heightProperty().addListener((obs, o, n)-> resizeHandler.run());

        //run once to set the initial state
        resizeHandler.run();
    }
}
