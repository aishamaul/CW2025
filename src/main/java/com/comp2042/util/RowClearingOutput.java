package com.comp2042.util;

import java.util.List;

/**
 * A data container holding the result of a row clearing operation.
 * <p>
 *     Encapsulates the modified board matrix, the number of lines removed,
 *     and the indices of the removed rows for use by the game core and UI.
 * </p>
 */
public class RowClearingOutput {

    private final int linesRemoved;
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
