package com.comp2042.util.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Singleton manager for controlling the background music.
 * <p>
 *     Handles loading the music resource and playing it in an indefinite loop using JavaFX MediaPlayer.
 * </p>
 */
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

    /**
     * Retrieves the singleton instance of the music manager.
     * @return The single {@link BackgroundMusicManager} instance.
     */
    public static BackgroundMusicManager getInstance() {
        if (instance == null) {
            instance = new BackgroundMusicManager();
        }
        return instance;
    }

    /**
     * starts playing the background music loop.
     * <p>
     *     Safe to call multiple times, if already playing it continues.
     * </p>
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
}
