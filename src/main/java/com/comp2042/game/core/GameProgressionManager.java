package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameRuntimeManager;

/**
 * Manages the difficulty progression of the game.
 * <p>
 *     This class monitors the number of lines cleared and updates the game speed
 *     based on the current {@link GameMode} or standard Classic Mode rules.
 * </p>
 */
public class GameProgressionManager {

    private final GameRuntimeManager runtimeManager;
    private final java.util.function.Supplier<GameMode> modeSupplier;
    private final boolean isClassicMode;

    /**
     * Constructs a new GameProgressionManager
     * @param runtimeManager The manager controlling the game loop and speed.
     * @param modeSupplier A supplier providing the current GameMode.
     * @param isClassicMode Flag indicating if the standard classic rules apply.
     */
    public GameProgressionManager(GameRuntimeManager runtimeManager,
                                  java.util.function.Supplier<GameMode> modeSupplier,
                                  boolean isClassicMode) {
        this.runtimeManager = runtimeManager;
        this.modeSupplier = modeSupplier;
        this.isClassicMode = isClassicMode;
    }

    /**
     * Called whenever the total number of cleared lines changes.
     * Updates the game loop rate.
     * @param lines The total number of lines cleared so far.
     */
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
