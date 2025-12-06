package com.comp2042.game.core;

import com.comp2042.game.bricks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivePieceTest {

    private ActivePiece piece;
    private BoardGrid grid;

    private static class StubBrick implements Brick {
        @Override
        public List<int[][]> getShapeMatrix() {
            List<int[][]> shapes = new ArrayList<>();
            shapes.add(new int[][]{
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            });
            shapes.add(new int[][]{{0}});
            shapes.add(new int[][]{{0}});
            shapes.add(new int[][]{{0}});
            return shapes;
        }
    }

    @BeforeEach
    void setUp() {
        piece = new ActivePiece();
        grid = new BoardGrid(10, 20);
        piece.spawn(new StubBrick(), 4, 0);
    }

    @Test
    void testInitialSpawnPosition(){
        assertEquals(4, piece.getX());
        assertEquals(0, piece.getY());
    }

    @Test
    void testMove_Valid(){
        // move right into empty space
        boolean success = piece.move(1, 0, grid);

        assertTrue(success, "Should return true for valid move");
        assertEquals(5, piece.getX(), "X position should update");
    }

    @Test
    void testMove_CollisionWithWall(){
        piece.spawn(new StubBrick(), 6, 5);

        boolean success = piece.move(1, 0, grid);

        assertFalse(success, "Should return false when hitting right wall");
        assertEquals(6, piece.getX(), "Position should not change on failure");
    }

    @Test
    void testCalculateGhostY(){
        int ghostY = piece.calculateGhostY(grid);

        assertEquals(18, ghostY, "Ghost piece should be calculated at the floor level");
    }
}
