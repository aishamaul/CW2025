package com.comp2042.game.bricks;

import java.util.List;

/**
 * Represents a Tetromino game piece
 * Provides access to the shape matrices for all possible rotation states
 */
public interface Brick {

    /**
     * @return a list of 2D arrays representing the 4 rotation states of the brick
     */
    List<int[][]> getShapeMatrix();
}
