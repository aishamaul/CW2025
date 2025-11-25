package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) used to pass game state to the UI renderer
 * Immutable class that holds a snapshot of the current brick and its position
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextBricksData;
    private final int ghostYPosition;
    private final int[][] holdBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostYPosition, List<int[][]> nextBricksData, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBricksData = nextBricksData;
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
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

    public List<int[][]> getNextBricksData() {
        List<int[][]> copy = new ArrayList<>();
        for(int[][] matrix:nextBricksData){
            copy.add(MatrixOperations.copy(matrix));
        }
        return copy;
    }

    public int[][] getHoldBrickData() {
        return holdBrickData != null ? MatrixOperations.copy(holdBrickData) : null;
    }
}
