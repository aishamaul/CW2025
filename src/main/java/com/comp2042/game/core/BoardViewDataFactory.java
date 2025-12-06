package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.model.ViewData;

import java.util.ArrayList;
import java.util.List;

public class BoardViewDataFactory {

    public ViewData createViewData(ActivePiece activePiece,
                                   BoardGrid grid,
                                   BrickGenerator brickGenerator,
                                   HoldManager holdManager,
                                   boolean isBrickActive) {

        // next bricks shapes
        List<Brick> nextBricks = brickGenerator.getPeekNextBricks(3);
        List<int[][]> nextShapes = new ArrayList<>();
        for (Brick b : nextBricks) {
            nextShapes.add(b.getShapeMatrix().get(0));
        }

        // hold brick shape
        Brick heldBrick = holdManager.getHeldBrick();
        int[][] holdShape = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;

        // prepare active brick shape and ghost position
        int[][] currentShape = activePiece.getShape();

        // if brick is inactive (merged/not spawned), send empty shape to avoid rendering ghost
        if (isBrickActive) {
            // keep currentShape as is
        } else {
            currentShape = new int[currentShape.length][currentShape[0].length];
        }

        // calculate ghost position
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
}


