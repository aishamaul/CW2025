package com.comp2042.game.bricks;

import com.comp2042.util.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * A base implementation of the {@link Brick} interface.
 * <p>
 *     This abstract class manages the storage of rotation states (matrices) for a brick
 *     and provides a common implementation for retrieving them safely.
 * </p>
 */
public class AbstractBrick implements Brick{
    protected final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Retrieves the shape matrices for this brick.
     * <p>
     *     Returns a deep copy of the matrix list to ensure the internal state
     *     of the brick definition cannot be modified by external node.
     * </p>
     * @return A list of 2D integer arrays representing the rotation states.
     */
    @Override
    public List<int[][]>  getShapeMatrix(){
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
