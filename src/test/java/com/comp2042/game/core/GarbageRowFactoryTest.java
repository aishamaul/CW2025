package com.comp2042.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GarbageRowFactoryTest {

    private final GarbageRowFactory factory = new GarbageRowFactory();

    @Test
    void testCreateLevel3Row_Dimensions(){
        int width = 10;
        int [] row = factory.createLevel3Row(width);

        assertEquals(width,row.length, "Generated row must match the requested width");
    }

    @Test
    void testCreateLevel3Row_ContentValues(){
        int width = 10;
        int[] row = factory.createLevel3Row(width);

        for (int cell : row) {
            assertTrue(cell >= 0 && cell <= 8, "Cell value " + cell + " is out of valid range (0-8)");
        }
    }

    @Test
    void testCreateLevel3Row_MandatoryHoleLogic(){
        for (int i = 0; i < 50; i++) {
            int[] row = factory.createLevel3Row(10);
            boolean hasHole = false;
            for (int cell : row) {
                if (cell == 0) {
                    hasHole = true;
                    break;
                }
            }
            assertTrue(hasHole, "Garbage row must contain at least one empty space (0)");
        }
    }
}
