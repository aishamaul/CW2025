package com.comp2042.util.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class BackgroundMusicManager {

    private static BackgroundMusicManager instance;
    private MediaPlayer mediaPlayer;

    private BackgroundMusicManager() {
        try {
            // load the music file
            URL resource = getClass().getResource("/audio/background music.mp3");
            if (resource != null) {
                // handle spaces in filename by converting to URI
                String source = resource.toURI().toString();
                Media media = new Media(source);
                mediaPlayer = new MediaPlayer(media);

                // set to loop indefinitely
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BackgroundMusicManager getInstance() {
        if (instance == null) {
            instance = new BackgroundMusicManager();
        }
        return instance;
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
}
