package com.comp2042.game.mode;

import com.comp2042.game.core.Board;
import com.comp2042.game.core.GameLoopManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Called every game frame.
     * @return true if gravity should happen (brick falls), false if frozen.
     */
    default boolean onGameTick(Board board) {
        return true;
    }

    default String getOverlayMessage(){
        return null;
    }

    default void onStart(Board board) {}

    default List<Point> onBrickMerged(Board board) {
        return new ArrayList<>();
    }
}
