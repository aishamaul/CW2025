package com.comp2042.game.mode;

import com.comp2042.game.bricks.BombBrick;
import com.comp2042.game.bricks.Level3BrickGenerator;
import com.comp2042.game.core.Board;
import com.comp2042.game.core.GameLoopManager;
import com.comp2042.game.core.GarbageRowFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the third (and final) challenge level.
 * <p>
 * Objective: Clear 25 lines.
 * Mechanics:
 * <ul>
 * <li>Starts with the board partially filled with garbage rows.</li>
 * <li>Introduces the {@link BombBrick} which clears a 3x3 area upon landing.</li>
 * </ul>
 * </p>
 */
public class ChallengeLevel3  implements GameMode{

    private static final int GOAL_LINES = 25;
    private final GarbageRowFactory garbageFactory = new GarbageRowFactory();


    @Override
    public String getName(){
        return "LEVEL 3";
    }

    @Override
    public String getDescription() {
        return "Chaos mode! Garbage blocks everywhere.\n" +
                "Plus a RED BOMB that clears a 3x3 area!\n" +
                "Survive and clear 25 lines to finish the challenge!";
    }

    /**
     * Initializes the level by setting a custom brick generator and filling the board with garbage.
     * @param board The game board to initialize.
     */
    @Override
    public void onStart(Board board) {
        // set the custom generator
        board.setBrickGenerator(new Level3BrickGenerator());

        // Fill 3/4 of the board (approx 18 rows) with garbage
        for (int i = 0; i < 12; i++) {
            int[] row = garbageFactory.createLevel3Row(10);
            board.insertRowAtBottom(row);
        }
    }

    /**
     * Updates the game speed.
     * @param totalLines  Total lines cleared.
     * @param loopManager Loop manager.
     */
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

    /**
     * Checks if the merged brick was a BombBrick and triggers an explosion if so.
     * @param board The current game board.
     * @return A list of points affected by the explosion.
     */
    @Override
    public List<Point> onBrickMerged(Board board) {
        if (board.getCurrentBrick() instanceof BombBrick) {
            int x = (int) board.getCurrentOffset().getX() + 1;
            int y = (int) board.getCurrentOffset().getY() + 1;
            // Return the points that were exploded
            return board.explode(x, y, 1);
        }
        return new ArrayList<>();
    }


}
