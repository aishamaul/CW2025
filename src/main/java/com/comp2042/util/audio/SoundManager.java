package com.comp2042.util.audio;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.AudioClip;

import javafx.scene.input.MouseEvent;import java.net.URL;

public class SoundManager {

    private static SoundManager instance;
    private AudioClip bonusSound;
    private AudioClip hoverSound;
    private AudioClip clickSound;
    private AudioClip hardDropSound;
    private AudioClip loseSound;
    private AudioClip moveSound;



    private SoundManager() {
        try {
            // load the bonus sound file
            URL resource = getClass().getResource("/audio/bonus.mp3");
            if (resource != null) {
                // audioClip is better for short sound effects than MediaPlayer
                bonusSound = new AudioClip(resource.toExternalForm());
                bonusSound.setVolume(0.8);
            }

            // load Hover Sound
            URL hoverRes = getClass().getResource("/audio/button hover.mp3");
            if (hoverRes != null) {
                hoverSound = new AudioClip(hoverRes.toExternalForm());
                hoverSound.setVolume(1.5);
            }

            // load Click Sound
            URL clickRes = getClass().getResource("/audio/press button.mp3");
            if (clickRes != null) {
                clickSound = new AudioClip(clickRes.toExternalForm());
                clickSound.setVolume(0.8);
            }

            // load Hard Drop Sound
            URL dropRes = getClass().getResource("/audio/hard drop.mp3");
            if (dropRes != null) {
                hardDropSound = new AudioClip(dropRes.toExternalForm());
                hardDropSound.setVolume(0.3);
            }

            // Load Lose Sound
            URL loseRes = getClass().getResource("/audio/lose.mp3");
            if (loseRes != null) {
                loseSound = new AudioClip(loseRes.toExternalForm());
                loseSound.setVolume(0.8);
            }

            //load move sound
            URL moveRes = getClass().getResource("/audio/move brick.mp3");
            if (moveRes != null) {
                moveSound = new AudioClip(moveRes.toExternalForm());
                moveSound.setVolume(0.2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playLineClearSound() {
        if (bonusSound != null) {
            bonusSound.play();
        }
    }

    public void playHoverSound() {
        if (hoverSound != null) hoverSound.play();
    }

    public void playClickSound() {
        if (clickSound != null) clickSound.play();
    }

    /**
     * Utility to attach sound events to all buttons in a scene root
     */
    public void attachButtonSounds(Node root) {
        if (root == null) return;

        // Attach to standard menu buttons
        root.lookupAll(".menuButton").forEach(node -> {
            if (node instanceof Button btn) {
                btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> playHoverSound());
                btn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> playClickSound());
            }
        });

        // Attach to pause button
        root.lookupAll(".pauseButton").forEach(node -> {
            if (node instanceof ToggleButton btn) {
                btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> playHoverSound());
                btn.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> playClickSound());
            }
        });
    }

    public void playHardDropSound() {
        if (hardDropSound != null) hardDropSound.play();
    }

    public void playLoseSound() {
        if (loseSound != null) loseSound.play();
    }

    public void playMoveSound() {
        if (moveSound != null) moveSound.play();
    }

}
