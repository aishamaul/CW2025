package com.comp2042.game.mode;

import com.comp2042.game.core.GameLoopManager;

/**
 * Implementation of the first challenge level.
 * <p>
 * Objective: Clear 20 lines.
 * Mechanic: The game speed increases progressively as lines are cleared.
 * </p>
 */
public class ChallengeLevel1 implements GameMode{

    private static final int GOAL_LINES = 20;

    @Override
    public String getName(){
        return "LEVEL 1";
    }

    @Override
    public String getDescription() {
        return "Clear 20 lines! Speed steadily increases.\nGet ready to warm up!";
    }

    /**
     * Updates the game speed based on the number of lines cleared.
     * <p>
     * Speed multiplier increases every 5 lines.
     * </p>
     *
     * @param totalLines  The total lines cleared.
     * @param loopManager The loop manager to update the rate.
     */
    @Override
    public void onLinesUpdated (int totalLines, GameLoopManager loopManager){
        // speed increases every 3 lines cleared
        double speedMultiplier = 1.0 + (totalLines/5) * 0.6;
        loopManager.setRate(speedMultiplier);
    }

    /**
     * Checks if the goal of 20 lines has been reached.
     * @param totalLines The total lines cleared.
     * @return true if totalLines >= 20.
     */
    @Override
    public boolean isWinConditionMet(int totalLines){
        return totalLines >= GOAL_LINES;
    }

    @Override
    public GameMode getNextLevel(){
        return new ChallengeLevel2();
    }
}
