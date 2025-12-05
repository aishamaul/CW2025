package com.comp2042.game.mode;

import com.comp2042.game.core.GameLoopManager;

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

    @Override
    public void onLinesUpdated (int totalLines, GameLoopManager loopManager){

        // speed increases every 3 lines cleared
        double speedMultiplier = 1.0 + (totalLines/5) * 0.6;
        loopManager.setRate(speedMultiplier);
    }

    @Override
    public boolean isWinConditionMet(int totalLines){
        return totalLines >= GOAL_LINES;
    }

    @Override
    public GameMode getNextLevel(){
        return new ChallengeLevel2();
    }
}
