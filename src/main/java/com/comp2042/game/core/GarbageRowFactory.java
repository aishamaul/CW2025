package com.comp2042.game.core;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Creates garbage rows used in challenge level 3.
 * <p>
 *     Garbage rows are lines of blocks with random holes that are pushed onto
 *     the board to increase difficulty
 * </p>
 */
public class GarbageRowFactory {

    /**
     * @param width The width of the row to generate.
     * @return An integer array representing the row, where 0 is empty and other numbers are colors.
     */
    public int[] createLevel3Row(int width){
        int[] newRow = new int[width];

        int mandatoryHole = ThreadLocalRandom.current().nextInt(width);

        for (int col = 0; col < width; col ++){
            if (col == mandatoryHole){
                newRow[col] = 0;
            } else{
                if (ThreadLocalRandom.current().nextDouble() < 0.4){
                    newRow[col] = 0;
                } else{
                    newRow[col] = ThreadLocalRandom.current().nextInt(1, 8);
                }
            }
        }
        return newRow;
    }
}
