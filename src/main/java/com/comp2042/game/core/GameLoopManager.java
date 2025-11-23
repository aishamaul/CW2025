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
}
