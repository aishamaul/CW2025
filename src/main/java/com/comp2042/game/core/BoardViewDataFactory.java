package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.model.ViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory class responsible for creating {@link ViewData} snapshots.
 * <p>
 *     This class extracts the necessary state from the active piece, board grid,
 *     generators, and hold managers to create an immutable data transfer object (DTO)
 *     used by the UI renderer.
 * </p>
 */
public class BoardViewDataFactory {

    /**
     * Creates a {@link ViewData} object representing the current state of the game components.
     * @param activePiece The current active piece.
     * @param grid The game board grid.
     * @param brickGenerator The generator managing the queue of next bricks.
     * @param holdManager The manager handling the held brick.
     * @param isBrickActive A flag indicating if there is currently a brick falling.
     * @return A populated {@link ViewData} instance.
     */
    public ViewData createViewData(ActivePiece activePiece,
                                   BoardGrid grid,
                                   BrickGenerator brickGenerator,
                                   HoldManager holdManager,
                                   boolean isBrickActive) {

        List<int[][]> nextShapes = extractNextShapes(brickGenerator);
        int[][] holdShape = extractHoldShape(holdManager);
        int[][] currentShape = extractActiveShape(activePiece, isBrickActive);

        int ghostY = activePiece.calculateGhostY(grid);

        return new ViewData(
                currentShape,
                activePiece.getX(),
                activePiece.getY(),
                ghostY,
                nextShapes,
                holdShape
        );
    }

    private List<int[][]> extractNextShapes(BrickGenerator brickGenerator) {
        List<Brick> nextBricks = brickGenerator.getPeekNextBricks(3);
        List<int[][]> nextShapes = new ArrayList<>();
        for (Brick b : nextBricks) {
            nextShapes.add(b.getShapeMatrix().get(0));
        }
        return nextShapes;
    }

    private int[][] extractHoldShape(HoldManager holdManager) {
        Brick heldBrick = holdManager.getHeldBrick();
        return (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;
    }

    private int[][] extractActiveShape(ActivePiece activePiece, boolean isBrickActive) {
        int[][] currentShape = activePiece.getShape();
        if (!isBrickActive) {
            // Return empty shape if inactive to avoid rendering ghost
            return new int[currentShape.length][currentShape[0].length];
        }
        return currentShape;
    }


}


