package com.comp2042.game.mode;

import com.comp2042.game.core.GameLoopManager;

public interface GameMode {
    /**
     * @return The display name of the level
     */
    String getName();

    /**
     * Called whenever lines are cleared to apply level specific rules
     */
    void onLinesUpdated(int totalLise, GameLoopManager loopManager);

    /**
     * Checks if the player has beaten the level
     */
    boolean isWinConditionMet(int totalLines);

    /**
     * Returns the next level to play, or null if  this is the last one
     */

    GameMode getNextLevel();
}
