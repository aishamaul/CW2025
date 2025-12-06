package com.comp2042.game.mode;

import com.comp2042.game.core.Board;
import com.comp2042.game.core.GameLoopManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the contract for different game levels or modes.
 * <p>
 *     Implementations of this interface control level specific behaviour such as
 *     winning conditions, scoring progression, speed changes and special mechanics
 * </p>
 */
public interface GameMode {
    /**
     * @return The display name of the level.
     */
    String getName();

    /**
     * @return A short description of the level's objectives and mechanics.
     */
    default String getDescription() {
        return "";
    }

    /**
     * Called whenever lines are cleared to apply level specific rules.
     */
    void onLinesUpdated(int totalLise, GameLoopManager loopManager);

    /**
     * Checks if the player has beaten the level.
     */
    boolean isWinConditionMet(int totalLines);

    /**
     * Returns the next level to play, or null if  this is the last one.
     */

    GameMode getNextLevel();

    /**
     * Called every game frame.
     * @return true if gravity should happen (brick falls), false if frozen.
     */
    default boolean onGameTick(Board board) {
        return true;
    }

    /**
     * Provides a message to overlay on the screen/
     *
     * @return A string message to display, "SHOW_TEXT" to trigger a specific animation, or null to hide.
     */
    default String getOverlayMessage(){
        return null;
    }

    /**
     * Called when the level is first initialized.
     * Useful for setting up the board state
     *
     * @param board The game board to initialize.
     */
    default void onStart(Board board) {}

    /**
     * Called immediately after a brick locks into place.
     * Used for mechanics that react to placement, like exploding bombs.
     *
     * @param board The current game board.
     * @return A list of points that were affected by the event.
     */
    default List<Point> onBrickMerged(Board board) {
        return new ArrayList<>();
    }
}
