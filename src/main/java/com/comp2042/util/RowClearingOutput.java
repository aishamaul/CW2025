package com.comp2042.util;

import java.util.List;

public class RowClearingOutput {

    public final int linesRemoved;
    private final int[][] newMatrix;
    private final List<Integer> clearedIndices;

    public RowClearingOutput(int linesRemoved, int[][] newMatrix, List<Integer> clearedIndices) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.clearedIndices = clearedIndices;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public List<Integer> getClearedIndices() {
        return clearedIndices;
    }
}
