package com.comp2042.game.core;

import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.util.audio.SoundManager;

/**
 * Encapsulates the logic for processing line clears.
 * <p>
 *     This involves checking the board for full rows, calculating scores,
 *     triggering sound effects, updating the UI, and running post-clear animations.
 * </p>
 */
public class LineClearProcessor {

    /**
     * Detects and processes any cleared lines on the board.
     * @param board The game board to check.
     * @param scoreEvaluator The engine to calculate points for the cleared lines.
     * @param viewAdapter The adapter to update the UI.
     * @param onComplete Callback to run after processing (and animations) is complete.
     */
    public void processLineClears(Board board,
                                  ScoreEvaluator scoreEvaluator,
                                  GameViewAdapter viewAdapter,
                                  Runnable onComplete){

        // row check on the board
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0){

            // play sound effect once, regardless of how many lines are cleared
            SoundManager.getInstance().playLineClearSound();

            scoreEvaluator.scoreLineClear(clearRow.getScoreBonus(), board.getScore());
            board.getScore().addLines(clearRow.getLinesRemoved());
            viewAdapter.showScoreNotification(clearRow.getScoreBonus());

            // animate then refresh background and callback
            viewAdapter.onLineClear(clearRow.getClearedIndices(), () ->{
                viewAdapter.refreshGameBackground(board.getBoardMatrix());
                onComplete.run();
            });
        } else{
            // no lines cleared, just refresh and callback
            viewAdapter.refreshGameBackground(board.getBoardMatrix());
            onComplete.run();
        }
    }
}
