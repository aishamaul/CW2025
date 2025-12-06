package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.mode.GameMode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.comp2042.ui.input.EventDispatcher;
import java.util.function.BiConsumer;

public class GameRuntimeManager {

    private final GameLoopManager gameLoopManager;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private final FreezeOverlayManager freezeOverlayManager;

    public GameRuntimeManager(Runnable onTickCallback) {
        this.gameLoopManager = new GameLoopManager(onTickCallback);
        this.freezeOverlayManager = null;
    }

    /**
     * Initializes a standard loop with game tick logic
     */
    public GameRuntimeManager(EventDispatcher dispatcher,
                              FreezeOverlayManager freezeOverlayManager,
                              BiConsumer<EventType, EventSource> moveDownAction,
                              java.util.function.Supplier<GameMode> modeSupplier) {

        this.freezeOverlayManager = freezeOverlayManager;

        this.gameLoopManager = new GameLoopManager(() -> {
            GameMode mode = modeSupplier.get();
            boolean applyGravity = dispatcher.onGameTick(mode);
            if (applyGravity) {
                moveDownAction.accept(EventType.DOWN, EventSource.THREAD);
            }
            if (freezeOverlayManager != null) {
                freezeOverlayManager.updateOverlay(mode);
            }
        });
    }

    public void startLoop() {
        gameLoopManager.play();
    }

    public void stopLoop() {
        gameLoopManager.stop();
    }

    public void pauseLoop() {
        gameLoopManager.pause();
    }

    public void setRate(double rate) {
        gameLoopManager.setRate(rate);
    }

    public void resetState() {
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        setRate(1.0);
    }

    public void setPause(boolean paused) {
        isPause.setValue(paused);
        if (paused) {
            gameLoopManager.pause();
        } else {
            gameLoopManager.play();
        }
    }

    public void setGameOver(boolean over) {
        isGameOver.setValue(over);
        if (over) {
            gameLoopManager.stop();
        }
    }

    public BooleanProperty isPauseProperty() {
        return isPause;
    }

    public BooleanProperty isGameOverProperty() {
        return isGameOver;
    }

    public GameLoopManager getGameLoopManager() {
        return gameLoopManager;
    }

}
