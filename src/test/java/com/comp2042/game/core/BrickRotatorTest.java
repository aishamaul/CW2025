package com.comp2042.game.core;

import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.bricks.RandomBrickGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class BrickRotatorTest {
    private BrickRotator rotator;
    private BoardGrid grid;
    private BrickGenerator generator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        grid = new BoardGrid(10, 20);
        generator = new RandomBrickGenerator();
        rotator.setBrick(generator.getBrick());
    }

    @Test
    void testRotationChangeShape(){
        int[][] initialShape = rotator.getCurrentShape();
        Point offset = new Point(5, 5);

        boolean success = rotator.tryRotate(grid, offset);

        assertTrue(success, "Rotation should be successful in empty space.");
        assertNotEquals(initialShape, rotator.getCurrentShape(), "Shape matrix should change after rotation.");
    }
}
