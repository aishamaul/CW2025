package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * A helper class that holds information about a specific rotation state of a brick.
 * <p>
 * This is used during the rotation calculation process to store the next shape
 * configuration and its rotation index so it can be tested for collisions before
 * being applied to the active piece.
 * </p>
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}
