package com.comp2042.util.audio;

import javafx.scene.media.AudioClip;

import java.net.URL;

public class SoundManager {

    private static SoundManager instance;
    private AudioClip bonusSound;

    private SoundManager() {
        try {
            // load the bonus sound file
            URL resource = getClass().getResource("/audio/bonus.mp3");
            if (resource != null) {
                // audioClip is better for short sound effects than MediaPlayer
                bonusSound = new AudioClip(resource.toExternalForm());
                bonusSound.setVolume(0.8);
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

}
