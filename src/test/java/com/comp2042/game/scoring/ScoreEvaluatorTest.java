package com.comp2042.game.scoring;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.MoveEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreEvaluatorTest {
    private ScoreEvaluator evaluator;
    private Score score;

    @BeforeEach
    void setUp() {
        evaluator = new ScoreEvaluator();
        score = new Score();
    }

    @Test
    void testScoreLineClear() {
        // 1 line cleared = 50 * 1 * 1 = 50
        evaluator.scoreLineClear(50, score);
        assertEquals(50, score.scoreProperty().get());
    }

    @Test
    void testScoreDrop(){
        //dropping 5 rows = 5*2 = 10
        evaluator.scoreDrop(5, score);
        assertEquals(10, score.scoreProperty().get());
    }

    @Test
    void testUserMOvementScore(){
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        evaluator.scoreMovement(event, score);
        assertEquals(1, score.scoreProperty().get(), "User movement should add 1 point.");
    }
}
