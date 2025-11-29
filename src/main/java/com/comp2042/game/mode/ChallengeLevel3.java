package com.comp2042.game.mode;

import com.comp2042.game.bricks.BombBrick;
import com.comp2042.game.bricks.Level3BrickGenerator;
import com.comp2042.game.core.Board;
import com.comp2042.game.core.GameLoopManager;

public class ChallengeLevel3  implements GameMode{

    private static final int GOAL_LINES = 25;

    @Override
    public String getName(){
        return "LEVEL 3";
    }

    @Override
    public void onStart(Board board) {
        // set the custom generator
        board.setBrickGenerator(new Level3BrickGenerator());

        // Fill 3/4 of the board (approx 18 rows) with garbage
        for (int i = 0; i < 12; i++) {
            board.addLevel3GarbageLine();
        }
    }

    @Override
    public void onLinesUpdated(int totalLines, GameLoopManager loopManager) {
        // slowly increase speed every 5 lines
        double speed = 1.0 + (totalLines / 5) * 0.1;
        loopManager.setRate(speed);
    }

    @Override
    public boolean isWinConditionMet(int totalLines) {
        return totalLines >= GOAL_LINES;
    }

    @Override
    public GameMode getNextLevel() {
        return null; // final level
    }

    @Override
    public void onBrickMerged(Board board) {
        if (board.getCurrentBrick() instanceof BombBrick) {
            int x = (int) board.getCurrentOffset().getX() + 1;
            int y = (int) board.getCurrentOffset().getY() + 1;
            // radius 1 means 3x3 area (center + 1 in all directions)
            board.explode(x, y, 1);
        }
    }


}
