package com.comp2042.game.scoring;

public class RowScoreCalculator {
    private static final int BASE_SCORE_MULTIPLIER = 50;
    public int calculateScoreBonus(int linesRemoved){
        return BASE_SCORE_MULTIPLIER * linesRemoved * linesRemoved;    }
}
