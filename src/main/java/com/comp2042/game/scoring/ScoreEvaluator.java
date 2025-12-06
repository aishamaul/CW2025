package com.comp2042.game.scoring;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.MoveEvent;

/**
 * Logic class responsible for calculating score updates based on game events.
 * Separates scoring rules from the main game loop.
 */
public class ScoreEvaluator {

    /**
     * Awards points for manual user movements (soft drop).
     * @param event The movement event.
     * @param score The score object to update.
     */
    public void scoreMovement(MoveEvent event, Score score){

        if (event.getEventSource() == EventSource.USER) {
            score.add(1);
        }
    }

    /**
     * Awards points for Hard Drops.
     * @param rowDropped The number of rows the brick was dropped instantly.
     * @param score The score object to update.
     */
    public void scoreDrop(int rowDropped, Score score){
        int points = rowDropped * 2;
        score.add(points);
    }

    /**
     * Awards points for clearing lines.
     *
     * @param bonus The pre-calculated bonus points.
     * @param score The score object to update.
     */
    public void scoreLineClear(int bonus, Score score){
        if (bonus>0){
            score.add(bonus);
        }
    }
}
