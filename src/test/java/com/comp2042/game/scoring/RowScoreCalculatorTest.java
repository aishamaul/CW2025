package com.comp2042.game.scoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RowScoreCalculatorTest {

    private RowScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RowScoreCalculator();
    }

    @Test
    void testCalculateScoreBonus_ZeroLines(){
        assertEquals(0, calculator.calculateScoreBonus(0), "0 lines should give 0 points");
    }

    @Test
    void testCalculateScoreBonus_OneLines(){
        assertEquals(50, calculator.calculateScoreBonus(1));
    }

    @Test
    void testCalculateScoreBonus_FourLines(){
        assertEquals(800, calculator.calculateScoreBonus(4), "4 lines (Tetris) should give 800 points");
    }

    @Test
    void testCalculateScoreBonus_Progression(){
        int score2 = calculator.calculateScoreBonus(2); // 200
        int score3 = calculator.calculateScoreBonus(3); // 450

        assertTrue(score3 > score2, "Score should increase non-linearly");
    }

}
