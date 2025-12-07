package com.comp2042.ui.view;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

/**
 * Handles automatic scaling of the game content to fit the window.
 * <p>
 * This class attaches listeners to the root pane dimensions and calculates a scale
 * transform for the content pane to ensure it fills the window while maintaining
 * aspect ratio.
 * </p>
 */
public class WindowScaler {
    private static final double DESIGN_WIDTH = 600.0;
    private static final double DESIGN_HEIGHT = 700.0;

    /**
     * Binds the content pane's scale to the root pane's size.
     * @param rootPane    The parent container that changes size.
     * @param contentPane The game content to be scaled.
     */
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

            // center the contentPane
            double scaledWidth = DESIGN_WIDTH * scaleFactor;
            double scaledHeight = DESIGN_HEIGHT * scaleFactor;

            double offsetX = (windowWidth - scaledWidth) / 2;
            double offsetY = (windowHeight - scaledHeight) / 2;

            contentPane.setTranslateX(offsetX);
            contentPane.setTranslateY(offsetY);
        };

        // bind the listener to the root pane's dimensions
        rootPane.widthProperty().addListener((obs, o, n)-> resizeHandler.run());
        rootPane.heightProperty().addListener((obs, o, n)-> resizeHandler.run());

        //run once to set the initial state
        resizeHandler.run();
    }
}
