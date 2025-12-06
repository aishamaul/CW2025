package com.comp2042.game.core;

import com.comp2042.util.MatrixOperations;
import com.comp2042.util.RowClearer;
import com.comp2042.util.RowClearingOutput;

/**
 * Represent the 2D data structure of the Tetris board.
 * <p>
 * This class manages the integer matrix representing the state of the game board (which blocks are filled),
 * handles collision detection between the active piece and the walls/floor/static blocks,
 * and executes board modifications like merging pieces, clearing rows, and adding garbage lines.
 * </p>
 */


public class BoardGrid {
    private final int width;
    private final int height;
    private int[][] matrix;
    private final RowClearer rowClearer;

    /**
     * Constructs a new BoardGrid with the specified dimensions.
     *
     * @param width The width of the grid in columns.
     * @param height The height of the grid in rows.
     */

    public BoardGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new int[height][width];
        this.rowClearer = new RowClearer();
    }

    /**
     * Checks if a specific brick shape at a given position intersects with any existing bricks
     * or goes out of the boundaries.
     * @param brickShape The 2D array representing the shape of the brick.
     * @param x The x-coordinate of the brick's top left corner.
     * @param y The y-coordinate of the brick;s top left corner.
     * @return true if the brick collides with something or is out of bounds; false otherwise.
     */

    public boolean intersects(int[][] brickShape, int x, int y) {
        return MatrixOperations.intersect(matrix, brickShape, x, y);
    }

    /**
     * Merge a brick into the board matrix, effectively locking it in place.
     * This permanently changes the state of the board.
     * @param brickShape The 2D array representing the shape of the brick.
     * @param x The x-coordinate where the brick is locked.
     * @param y The y-coordinate where the brick is locked.
     */

    public void merge(int[][] brickShape, int x, int y) {
        this.matrix = MatrixOperations.merge(matrix, brickShape, x, y);
    }

    /**
     * Scans the board for full rows and clears them.
     *
     *
     * @return A {@link RowClearingOutput} object containing details about the operation,
     * including the number of lines cleared, the indices of cleared rows, and the new board matrix.
     */
    public RowClearingOutput clearRows() {
        RowClearingOutput output = rowClearer.checkRemoving(matrix);
        this.matrix = output.getNewMatrix();
        return output;
    }

    /**
     * Resets the entire grid to an empty state.
     */
    public void clear() {
        this.matrix = new int[height][width];
    }

    /**
     * Returns a copy of the current board matrix.
     *
     * @return A deep copy of the 2D integer array representing the board.
     */
    public int[][] getMatrix() {
        return MatrixOperations.copy(matrix);
    }

    public int getWidth() {return width; }
    public int getHeight() {return height; }


    /**
     * Calculates the y position where the brick would land if dropped instantly from the given coordinates.
     * This is primarily used for rendering the ghost piece.
     *
     * @param shape The shape matrix of the current brick.
     * @param startX The current X position.
     * @param startY The current Y position.
     * @return The Y coordinate representing the lowest valid position for the brick.
     */
    public int calculateDropPosition(int[][] shape, int startX, int startY){
        int ghostY = startY;
        //simulate dropping until collision
        while(!intersects(shape, startX, ghostY + 1)){
            ghostY ++;
        }
        return ghostY;
    }


    /**
     * Shifts all rows up by one and inserts a generic garbage line at the bottom.
     * Used for game modes or events that penalize the player.
     *
     * @return true if the operation pushes blocks off the top of the board (Game Over condition), false otherwise.
     */
    public boolean addGarbageLine(){
        // check if top row has blocks (game over condition)
        for (int col = 0; col < width; col ++){
            if (matrix[0][col] != 0) return true;
        }

        // shift rows up
        for (int row = 0; row < height - 1; row++){
            matrix[row] = matrix[row+1];
        }

        // generate solid garbage row
        int[]  newRow = new int [width];
        for (int col = 0; col< width; col++){
            newRow[col] = 8;
        }
        matrix[height - 1] = newRow;
        return false;
    }

    /**
     * Shifts all rows up and inserts a specific row configuration at the bottom.
     *
     * @param newRow An array representing the new bottom row. Must match the board width.
     * @return true if the operation pushes blocks off the top of the board (Game Over condition).
     * @throws IllegalArgumentException if the provided row width does not match the board width.
     */
    public boolean insertRowAtBottom(int[] newRow){
        if (newRow.length != width){
            throw new IllegalArgumentException("Row width does not match board width");
        }

        for (int col = 0; col < width; col ++){
            if (matrix[0][col] !=0) return true;
        }

        for (int row = 0; row < height - 1; row ++){
            matrix[row] = matrix[row + 1];
        }

        matrix[height - 1] = newRow;
        return false;
    }

    /**
     * Clears a single cell at the specified coordinates.
     * Used for specialized game mechanics like bombs or explosions.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void clearCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            matrix[y][x] = 0;
        }
    }
}
