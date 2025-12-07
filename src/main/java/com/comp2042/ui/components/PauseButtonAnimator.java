package com.comp2042.ui.components;

import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Adds interactive visual effects to the pause button.
 * <p>
 * This class attaches listeners to the button to create a neon glow effect
 * that reacts to mouse hover and click states.
 * </p>
 */
public class PauseButtonAnimator {
    private final ToggleButton pauseButton;
    private DropShadow neonGlow;

    public PauseButtonAnimator(ToggleButton pauseButton) {
        this.pauseButton = pauseButton;
    }

    /**
     * Initializes and starts the animation logic.
     * <p>
     * Sets up the initial effect and binds listeners to the button's hover and pressed properties.
     * </p>
     */
    public void start() {
        if (pauseButton == null) {
            return;
        }

        neonGlow = new DropShadow();
        neonGlow.setColor(Color.web("#8ee7ff"));
        neonGlow.setRadius(14);
        neonGlow.setSpread(0.45);
        neonGlow.setOffsetX(0);
        neonGlow.setOffsetY(0);

        pauseButton.setEffect(neonGlow);

        pauseButton.hoverProperty().addListener((obs, wasHovering, isHovering) ->
                applyGlow(isHovering, pauseButton.isPressed())
        );

        pauseButton.pressedProperty().addListener((obs, wasPressed, isPressed) ->
                applyGlow(pauseButton.isHover(), isPressed)
        );

        applyGlow(false, false);
    }

    private void applyGlow(boolean isHovering, boolean isPressed) {
        if (neonGlow == null) {
            return;
        }

        if (isPressed) {
            neonGlow.setColor(Color.web("#c1f6ff"));
            neonGlow.setRadius(20);
            neonGlow.setSpread(0.68);
        } else if (isHovering) {
            neonGlow.setColor(Color.web("#a9f3ff"));
            neonGlow.setRadius(18);
            neonGlow.setSpread(0.58);
        } else {
            neonGlow.setColor(Color.web("#8ee7ff"));
            neonGlow.setRadius(12);
            neonGlow.setSpread(0.4);
        }
    }

}
