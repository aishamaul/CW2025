package com.comp2042.game.core;

import java.util.concurrent.ThreadLocalRandom;

public class GarbageRowFactory {

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
