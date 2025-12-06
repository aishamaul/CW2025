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
    private AudioClip winSound;
    private AudioClip holdSound;
    private AudioClip garbageRowSound;
    private AudioClip explosionSound;
    private AudioClip landSound;


    private SoundManager() {
        try {
            bonusSound = loadClip("/audio/bonus.mp3", 0.8);
            hoverSound = loadClip("/audio/button hover.mp3", 1.5);
            clickSound = loadClip("/audio/press button.mp3", 0.8);
            hardDropSound = loadClip("/audio/hard drop.mp3", 0.6);
            loseSound = loadClip("/audio/lose.mp3", 0.8);
            moveSound = loadClip("/audio/move brick.mp3", 0.2);
            winSound = loadClip("/audio/win.mp3", 1.0);
            holdSound = loadClip("/audio/hold.mp3", 0.8);
            garbageRowSound = loadClip("/audio/garbage row.mp3", 1.0);
            explosionSound = loadClip("/audio/explosion.mp3", 1.0);
            landSound = loadClip("/audio/land.mp3", 0.2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper to load audio clips safely
     */
    private AudioClip loadClip(String path, double volume) {
        URL resource = getClass().getResource(path);
        if (resource != null) {
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(volume);
            return clip;
        }
        return null;
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

    public void playWinSound() {
        if (winSound != null) winSound.play();
    }

    public void playHoldSound() {
        if (holdSound != null) holdSound.play();
    }

    public void playGarbageRowSound() {
        if (garbageRowSound != null) garbageRowSound.play();
    }

    public void playExplosionSound() {
        if (explosionSound != null) explosionSound.play();
    }

    public void playLandSound() {
        if (landSound != null) landSound.play();
    }

}
