package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

import java.util.List;

/**
 * A data transfer object representing the result of a row clearing operation.
 * <p>
 * This class encapsulates all the changes that occur when rows are completed and removed
 * from the board, including the number of lines removed, the updated board state,
 * the score bonus awarded, and the indices of the cleared rows for animation purposes.
 * </p>
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedIndices;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedIndices) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedIndices = clearedIndices;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public List<Integer> getClearedIndices() { return clearedIndices; }
}
