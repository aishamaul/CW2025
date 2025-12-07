package com.comp2042.util;

import com.comp2042.game.core.BoardGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the logic for processing explosions on the game board.
 * <p>
 *     This utility calculates which cells on the grid are affected by an explosion
 *     originating from a specific point and clears them.
 * </p>
 */
public class ExplosionManager {
    public List<Point> processExplosion(BoardGrid grid, int centerX, int centerY, int radius){
        List<Point> explodedPoints = new ArrayList<>();
        int [][] currentMatrix = grid.getMatrix();
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int i = centerX - radius; i <= centerX + radius; i++) {
            for (int j = centerY - radius; j <= centerY + radius; j++) {
                // boundary checks
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    // only add if there was a block there
                    if (currentMatrix[j][i] != 0){
                        explodedPoints.add(new Point(i, j));
                        grid.clearCell(i, j);
                    }

                }
            }
        }
        return explodedPoints;
    }
}
