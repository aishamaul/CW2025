package com.comp2042.ui.render;

import javafx.scene.shape.Rectangle;

/**
 * Responsible for applying visual styles to brick components.
 * <p>
 * This class translates integer color codes from the game model into JavaFX CSS style classes,
 * ensuring consistent coloring and effects for bricks across the UI.
 * </p>
 */
public class BrickStyler {


    public void applyBrickStyle(Rectangle rectangle, int colorCode) {
        rectangle.getStyleClass().removeIf(name -> name.startsWith("brick-") || name.equals("brick"));
        rectangle.getStyleClass().add("brick");
        if (colorCode <= 0) {
            rectangle.getStyleClass().add("brick-empty");
        } else if (colorCode <= 9) {
            rectangle.getStyleClass().add("brick-" + colorCode);
        } else {
            rectangle.getStyleClass().add("brick-unknown");
        }
    }
}

