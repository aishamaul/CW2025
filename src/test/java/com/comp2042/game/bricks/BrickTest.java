package com.comp2042.game.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Brick hierarchy using Equivalence Classes.
 * Verify that all Brick implementations behave consistently.
 */


public class BrickTest {

    @Test
    void testStandardBricksHaveFourRotations(){
        Brick[] bricks = {new JBrick(), new LBrick(), new TBrick()};

        for (Brick brick : bricks) {
            List<int[][]> shapes = brick.getShapeMatrix();
            assertEquals(4, shapes.size(), brick.getClass().getSimpleName() + " should have 4 rotation states");
        }

    }


    @Test
    void testSymmetricalBricksHaveTwoRotations(){
        Brick[] twoStateBricks = { new IBrick(), new SBrick(), new ZBrick()};

        for (Brick brick : twoStateBricks) {
            assertEquals(2, brick.getShapeMatrix().size(),
                    brick.getClass().getSimpleName() + " should have 2 rotation states");
        }
    }

    @Test
    void testOBrickHasOneRotation() {
        Brick oBrick = new OBrick();

        assertEquals(1, oBrick.getShapeMatrix().size(),
                "OBrick should have exactly 1 rotation state");
    }

    @Test
    void testBrickMatrixDimensions() {
        Brick[] samples = { new TBrick(), new IBrick(), new OBrick() };

        for (Brick brick : samples) {
            for (int[][] matrix : brick.getShapeMatrix()) {
                assertEquals(4, matrix.length);
                assertEquals(4, matrix[0].length);
            }
        }
    }

    @Test
    void testBombBrickStructure() {
        Brick bomb = new BombBrick();
        boolean foundBombId = false;

        for (int[][] matrix : bomb.getShapeMatrix()) {
            for (int[] row : matrix) {
                for (int cell : row) {
                    if (cell == 9) foundBombId = true;
                }
            }
        }
        assertTrue(foundBombId, "BombBrick must contain the unique cell ID 9");
    }
}
