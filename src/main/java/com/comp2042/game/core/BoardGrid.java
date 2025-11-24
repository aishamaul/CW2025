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
}
