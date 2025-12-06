package com.comp2042.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersect_NoCollision(){
        int[][] matrix = new int [][]{
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
        };

        int[][] brick = new int [][]{
                {0,1,0},
                {1,1,1},
                {0,0,0}
        };

        boolean intersects = MatrixOperations.intersect(matrix, brick, 0, 0);
        assertFalse(intersects, "Should not intersect with an empty board.");
    }

    @Test
    void testIntersect_CollisionWithWall(){
        int[][] matrix = new int [][] { {0,0}, {0,0}};
        int [][] brick = new int [][] { {1,1}, {1,1}};

        boolean intersects = MatrixOperations.intersect(matrix, brick, -1, 0);

        assertTrue(intersects, "Should intersect with the left wall.");
    }

    @Test
    void testIntersect_CollisionWithFloor(){
        int[][] matrix = new int [][] { {0,0}, {0,0}};
        int [][] brick = new int [][] { {1,1}, {1,1}};

        boolean intersects = MatrixOperations.intersect(matrix, brick, 0, 1);

        assertTrue(intersects, "Should intersect with the floor.");
    }

    @Test
    void testIntersect_CollisionWithAnotherBrick(){
        int[][] matrix = new int[][]{
                {0,0,0,0},
                {0,0,0,0},
                {1,1,1,1}
        };

        int [][] brick = new int [][] {
                {0,2,0},
                {2,2,2},
                {0,0,0}
        };

        boolean intersects = MatrixOperations.intersect(matrix, brick, 0, 1);
        assertTrue(intersects, "Should intersect with the other brick in the matrix.");
    }

    @Test
    void testIntersect_FloorBoundary(){
        int rows = 20;
        int cols = 10;
        int [][] matrix = new int [rows][cols];

        int[][] brick = {
                {0,0,0,0},
                {1,1,1,1}, // the brick data is in the second row of the shape 4x4
                {0,0,0,0},
                {0,0,0,0}
        };

        boolean intersects = MatrixOperations.intersect(matrix, brick, 0, rows - 2);
        assertFalse(intersects, "Brick should fit exactly at the bottom floor boundary");

    }

    @Test
    void testIntersect_WallBoundaries(){
        int[][] matrix = new int[10][10];
        int[][] brick = { {1} }; // 1x1 brick

        // left Boundary: x = -1 should intersect (out of bounds)
        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0), "Should intersect left wall boundary");

        // right Boundary: x = 10 should intersect (out of bounds for width 10)
        assertTrue(MatrixOperations.intersect(matrix, brick, 10, 0), "Should intersect right wall boundary");

        // inside boundary: x = 0 and x = 9 should be fine
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0));
        assertFalse(MatrixOperations.intersect(matrix, brick, 9, 0));
    }

    @Test
    void testCopy_DeepCopyLogic(){
        int[][] original = { {1, 2}, {3, 4} };
        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original, copy);
        assertNotSame(original, copy, "The array objects should be distinct");

        copy[0][0] = 99;
        assertEquals(1, original[0][0], "Original matrix should not be affected by changes to the copy");
    }

    @Test
    void testMerge_Logic(){
        int[][] board = { {0, 0}, {0, 0} };
        int[][] brick = { {1, 1}, {0, 0} };

        int[][] result = MatrixOperations.merge(board, brick, 0, 0);

        assertEquals(1, result[0][0]);
        assertEquals(1, result[0][1]);
        assertEquals(0, result[1][0]);
    }
}
