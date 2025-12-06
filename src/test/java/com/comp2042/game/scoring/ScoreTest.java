package com.comp2042.game.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialState() {
        assertEquals(0, score.scoreProperty().get());
        assertEquals(0, score.linesProperty().get());
    }

    @Test
    void testAddScore(){
        score.add(100);
        assertEquals(100, score.scoreProperty().get());

        score.add(50);
        assertEquals(150, score.scoreProperty().get());
    }

    @Test
    void testAddLines() {
        score.addLines(4);
        assertEquals(4, score.linesProperty().get());
    }

    @Test
    void testReset() {
        score.add(500);
        score.addLines(10);

        score.reset();

        assertEquals(0, score.scoreProperty().get(), "Score should reset to 0");
        assertEquals(0, score.linesProperty().get(), "Lines should reset to 0");
    }
}
