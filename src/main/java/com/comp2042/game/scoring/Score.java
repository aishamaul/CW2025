package com.comp2042.game.scoring;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A data model representing the player's current score and progress.
 * <p>
 * This class uses JavaFX Properties to allow the UI to observe and automatically
 * update when the score or line count changes.
 * </p>
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    private final IntegerProperty lines = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty linesProperty() { return lines;}

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void addLines(int count){ lines.setValue(lines.getValue() + count); }

    public void reset() {
        score.setValue(0);
        lines.setValue(0);
    }
}
