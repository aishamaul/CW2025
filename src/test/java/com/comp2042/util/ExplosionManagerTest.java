package com.comp2042.util;

import com.comp2042.game.core.BoardGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExplosionManagerTest {

    private ExplosionManager explosionManager;
    private BoardGrid grid;

    @BeforeEach
    void setUp() {
        explosionManager = new ExplosionManager();
        grid = new BoardGrid(10, 20);
    }

    @Test
    void testExplosionInCenter(){
        // place bricks in a 3x3 area center
        // center at 5,5, radis 1 covers 4,4 to 6,6
        grid.merge(new int[][]{{1}}, 5,5); //center
        grid.merge(new int[][]{{1}}, 4, 5); // left
        grid.merge(new int[][]{{1}}, 6, 5); // right

        List<Point> points = explosionManager.processExplosion(grid, 5, 5, 1);
        assertEquals(3, points.size(), "Should destroy exactly 3 blocks in range");
        assertEquals(0, grid.getMatrix()[5][5], "Center cell should be cleared");

    }

    @Test
    void testExplosionAtBoundary_RightWall(){
        // boundary condition testing: right wall (x=9)
        grid.merge(new int[][]{{1}}, 9,5);

        // logic coverage: ensure loop doesn't exceed width
        List<Point> points = explosionManager.processExplosion(grid, 9, 5,2);

        assertEquals(1, points.size());
        assertEquals(0, grid.getMatrix()[5][9]);
    }


    @Test
    void testExplosionEmptyArea(){
        //special value testing: empty set
        List<Point> points = explosionManager.processExplosion(grid, 5, 5, 1);
        assertTrue(points.isEmpty(), "Explosion in empty air should return empty list");
    }

}
