package com.comp2042.game.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGridTest {

    @Test
    void insertRowAtBottomShiftsRowsWhenBoardHasSpace(){
        BoardGrid grid = new BoardGrid(4, 3);

        int[] firstRow = {1, 1, 1, 1};
        int[] secondRow = {2, 2, 2, 2};

        assertFalse(grid.insertRowAtBottom(firstRow), "First insert should succeed without triggering game over.");
        assertFalse(grid.insertRowAtBottom(secondRow), "Second insert should also succeed when top row is empty.");

        int[][] matrix = grid.getMatrix();
        assertArrayEquals(firstRow, matrix[1], "Existing rows should shift upward when a new row is inserted.");
        assertArrayEquals(secondRow, matrix[2], "Newest row should occupy the bottom after insertion.");
    }

    @Test
    void insertRowAtBottomThrowsForInvalidWidth() {
        BoardGrid grid = new BoardGrid(3, 3);
        int[] invalidRow = {1, 2};

        assertThrows(IllegalArgumentException.class, () -> grid.insertRowAtBottom(invalidRow), "Inserting a row with a mismatched width should throw an exception.");

    }

    @Test
    void insertRowAtBottomDetectsGameOverWhenTopRowOccupied(){
        BoardGrid grid = new BoardGrid(2, 2);

        // fill every row with garbage so the top row is occupied
        grid.addGarbageLine();
        grid.addGarbageLine();
        grid.addGarbageLine();

        int[][] beforeInsert = grid.getMatrix();
        assertTrue(grid.insertRowAtBottom(new int[]{5, 5}), "Inserting when the top row is filled should signal game over.");
        assertArrayEquals(beforeInsert, grid.getMatrix(), "Matrix should remain unchanged when insertion is blocked.");
    }

    @Test
    void addGarbageLineAddSolidRowAndShiftsExistingRows(){
        BoardGrid grid = new BoardGrid(3, 3);

        int[] baseRow = {1, 0, 1};
        grid.insertRowAtBottom(baseRow);

        assertFalse(grid.addGarbageLine(), "Adding a garbage line to an empty top row should not trigger game over.");

        int[][] matrix = grid.getMatrix();
        assertArrayEquals(baseRow, matrix[1], "Existing rows should be shifted upward when garbage is added.");
        assertArrayEquals(new int[]{8, 8, 8}, matrix[2], "Garbage rows should be filled with solid blocks (value 8).");
    }

    @Test
    void addGarbageLinesSignalsGameOverWhenTopRowOccupied(){
        BoardGrid grid = new BoardGrid(1, 2);

        // two calls ensure the single column in the top row becomes occupied
        grid.addGarbageLine();
        grid.addGarbageLine();

        assertTrue(grid.addGarbageLine(), "Once the top row has a block, adding another garbage line should report game over.");
    }

}
