package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameLoopManager {

    private final Timeline timeLine;

    public GameLoopManager(Runnable onTick) {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GameConfig.GAME_LOOP_INTERVAL_MS),
                ae -> onTick.run()
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
    }

    public void play() {
        timeLine.play();
    }

    public void pause() {
        timeLine.pause();
    }

    public void stop() {
        timeLine.stop();
    }

    /**
     * Sets the speed multiplier for the game loop
     * 1.0 is normal speed, 2.0 is double speed etc
     * @param rate The new rate mutiplier
     */
    public void setRate(double rate) {
        timeLine.setRate(rate);
    }
}
