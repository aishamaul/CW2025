package com.comp2042.game.core;

import com.comp2042.model.NextShapeInfo;
import com.comp2042.game.bricks.Brick;

import java.awt.*;

/**
 * Handles the rotation logic for bricks on the Tetris board.
 * It attempts to rotate the brick and applies wall kicks if necessary.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Attempts to rotate the current brick
     * Tries the standard rotation first, then applies wall kicks if needed
     *
     * @param grid The current state of the board grid
     * @param currentOffset The x, y coordinates of the brick on the board
     * @return true if rotation succeeded (possibly with a kick), false if rotation is not possible
     */
    public boolean tryRotate(BoardGrid grid, Point currentOffset) {
        NextShapeInfo nextShapeInfo = getNextShape();
        int[][] nextMatrix = nextShapeInfo.getShape();
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();

        int[] kickOffsets = {0, 1, -1, 2, -2};

        for (int kick : kickOffsets) {
            int newX = currentX + kick;
            if (!grid.intersects(nextMatrix, newX, currentY)) {
                setCurrentShape(nextShapeInfo.getPosition());
                currentOffset.translate(kick,0);
                return true;
            }
        }
        return false;
    }

    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    public Brick getBrick(){
        return brick;
    }


}
