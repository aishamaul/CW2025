package com.comp2042.game.core;

import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.ui.view.GameViewAdapter;

public class LineClearProcessor {

    public void processLineClears(Board board,
                                  ScoreEvaluator scoreEvaluator,
                                  GameViewAdapter viewAdapter,
                                  Runnable onComplete){

        // row check on the board
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0){
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
