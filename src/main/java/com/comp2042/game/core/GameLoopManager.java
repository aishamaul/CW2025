package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the main game clock/timer.
 * <p>
 *     This class wraps a JavaFX {@link Timeline} to provide a controllable game loop.
 *     It allows the game to be paused, stopped, or have its speed adjusted dynamically.
 * </p>
 */
public class GameLoopManager {

    private final Timeline timeLine;

    /**
     * Creates a new GameLoopManager.
     *
     * @param onTick The {@link Runnable} to execute on every frame of the loop.
     */
    public GameLoopManager(Runnable onTick) {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GameConfig.GAME_LOOP_INTERVAL_MS),
                ae -> onTick.run()
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts or resumes the game loop.
     */
    public void play() {
        timeLine.play();
    }

    /**
     * Pauses the game loop. State is preserved.
     */
    public void pause() {
        timeLine.pause();
    }

    /**
     * Stops the game loop completely.
     */
    public void stop() {
        timeLine.stop();
    }

    /**
     * Sets the speed multiplier for the game loop.
     * 1.0 is normal speed, 2.0 is double speed etc.
     * @param rate The new rate multiplier.
     */
    public void setRate(double rate) {
        timeLine.setRate(rate);
    }
}
