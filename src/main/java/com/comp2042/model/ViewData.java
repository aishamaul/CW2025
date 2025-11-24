package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Data Transfer Object (DTO) used to pass game state to the UI renderer
 * Immutable class that holds a snapshot of the current brick and its position
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }
}
