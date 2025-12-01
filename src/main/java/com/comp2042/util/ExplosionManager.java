package com.comp2042.util;

import com.comp2042.game.core.BoardGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
