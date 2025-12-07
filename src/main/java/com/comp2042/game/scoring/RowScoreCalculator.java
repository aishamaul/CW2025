package com.comp2042.game.scoring;

/**
 * Calculates the score bonus awarded for clearing rows.
 * <p>
 * This class implements the scoring formula for line clears, applying a non-linear
 * multiplier to reward clearing multiple lines at once.
 * </p>
 */
public class RowScoreCalculator {
    private static final int BASE_SCORE_MULTIPLIER = 50;
    public int calculateScoreBonus(int linesRemoved){
        return BASE_SCORE_MULTIPLIER * linesRemoved * linesRemoved;    }
}
