package com.comp2042.game.core;

import com.comp2042.util.MatrixOperations;
import com.comp2042.util.RowClearer;
import com.comp2042.util.RowClearingOutput;

public class BoardGrid {
    private final int width;
    private final int height;
    private int[][] matrix;
    private final RowClearer rowClearer;

    public BoardGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new int[height][width];
        this.rowClearer = new RowClearer();
    }

    public boolean intersects(int[][] brickShape, int x, int y) {
        return MatrixOperations.intersect(matrix, brickShape, x, y);
    }

    public void merge(int[][] brickShape, int x, int y) {
        this.matrix = MatrixOperations.merge(matrix, brickShape, x, y);
    }

    public RowClearingOutput clearRows() {
        RowClearingOutput output = rowClearer.checkRemoving(matrix);
        this.matrix = output.getNewMatrix();
        return output;
    }

    public void clear() {
        this.matrix = new int[height][width];
    }

    public int[][] getMatrix() {
        return MatrixOperations.copy(matrix);
    }

    public int getWidth() {return width; }
    public int getHeight() {return height; }

    public int calculateDropPosition(int[][] shape, int startX, int startY){
        int ghostY = startY;
        //simulate dropping until collision
        while(!intersects(shape, startX, ghostY + 1)){
            ghostY ++;
        }
        return ghostY;
    }

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
}
