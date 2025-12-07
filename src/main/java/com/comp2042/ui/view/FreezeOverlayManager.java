package com.comp2042.ui.view;

import com.comp2042.game.mode.GameMode;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Manages the visual overlays for the time freeze mechanic in level 2.
 * <p>
 * Controls the visibility and text of the countdown timer and the "TIME FREEZE!"
 * notification message.
 * </p>
 */
public class FreezeOverlayManager {
    private final Label timerLabel;
    private final Label overlayMessageLabel;
    private boolean wasFrozen = false;

    public FreezeOverlayManager(Label timerLabel, Label overlayMessageLabel) {
        this.timerLabel = timerLabel;
        this.overlayMessageLabel = overlayMessageLabel;
    }

    /**
     * Updates the overlay state based on the current game mode status.
     * @param currentGameMode The active {@link GameMode} to query for status messages.
     */
    public void updateOverlay(GameMode currentGameMode) {
        if (currentGameMode != null) {
            String status = currentGameMode.getOverlayMessage();

            // text display (bricks frozen, timer hidden)
            if ("SHOW_TEXT".equals(status)) {
                timerLabel.setVisible(false); // ensure numbers don't overlap

                if (!wasFrozen) {
                    showFreezeNotification(); // trigger the text animation once
                    wasFrozen = true;
                }
            }
            // countdown (bricks frozen, timer visible, text gone)
            else if (status != null) {
                timerLabel.setText(status);
                timerLabel.setVisible(true);
                wasFrozen = true;
            } else {
                timerLabel.setVisible(false);
                wasFrozen = false;
            }
        }
    }

    private void showFreezeNotification() {
        overlayMessageLabel.setOpacity(1.0);
        overlayMessageLabel.setVisible(true);
        overlayMessageLabel.toFront();

        FadeTransition fade = new FadeTransition(Duration.seconds(3), overlayMessageLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(0.5));
        fade.setOnFinished(e -> overlayMessageLabel.setVisible(false));
        fade.play();
    }
}
