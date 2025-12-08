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

    /**
     * Constructs a new ClearRow result object.
     *
     * @param linesRemoved   The count of lines that were cleared.
     * @param newMatrix      The updated state of the board grid.
     * @param scoreBonus     The points awarded for this clear.
     * @param clearedIndices The list of row indices that were cleared.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedIndices) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedIndices = clearedIndices;
    }

    /**
     * Gets the number of lines removed.
     * @return The line count.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the updated board matrix.
     * @return A copy of the 2D integer array.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score calculated for this clear.
     * @return The score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Gets the indices of the rows that were cleared.
     * @return A list of integer row indices.
     */
    public List<Integer> getClearedIndices() { return clearedIndices; }
}
