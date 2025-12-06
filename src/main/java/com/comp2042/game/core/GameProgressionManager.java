package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameRuntimeManager;

public class GameProgressionManager {

    private final GameRuntimeManager runtimeManager;
    private final java.util.function.Supplier<GameMode> modeSupplier;
    private final boolean isClassicMode;

    public GameProgressionManager(GameRuntimeManager runtimeManager,
                                  java.util.function.Supplier<GameMode> modeSupplier,
                                  boolean isClassicMode) {
        this.runtimeManager = runtimeManager;
        this.modeSupplier = modeSupplier;
        this.isClassicMode = isClassicMode;
    }

    public void onLinesUpdated(int lines) {
        GameMode currentMode = modeSupplier.get();
        GameLoopManager loopManager = runtimeManager.getGameLoopManager();

        if (loopManager == null) return;

        if (currentMode != null) {
            // delegate difficulty to Challenge Mode logic
            currentMode.onLinesUpdated(lines, loopManager);
        } else if (isClassicMode) {
            // apply classic mode progression rule: Increase speed every 5 lines
            double newRate = 1.0 + (lines / 5.0) * 0.5;
            runtimeManager.setRate(newRate);
        }
    }


}
