package com.comp2042.game.core;

import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

public class GameLifecycleManager {

    private final Board board;
    private final ScoreEvaluator scoreEvaluator;
    private final GameViewAdapter viewAdapter;

    public GameLifecycleManager(Board board, ScoreEvaluator scoreEvaluator, GameViewAdapter viewAdapter) {
        this.board = board;
        this.scoreEvaluator = scoreEvaluator;
        this.viewAdapter = viewAdapter;
    }

    public DownData processTurnEnd() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            scoreEvaluator.scoreLineClear(clearRow.getScoreBonus(), board.getScore());

            board.getScore().addLines(clearRow.getLinesRemoved());

            viewAdapter.showScoreNotification(clearRow.getScoreBonus());

            viewAdapter.onLineClear(clearRow.getClearedIndices(), ()->{
                viewAdapter.refreshGameBackground(board.getBoardMatrix());
            });
        } else {
            viewAdapter.refreshGameBackground(board.getBoardMatrix());
        }
        if (board.createNewBrick()) {
            viewAdapter.gameOver();
        }
        return new DownData(clearRow, board.getViewData());
    }

    public void handleNewGame() {
        board.newGame();
        viewAdapter.refreshGameBackground(board.getBoardMatrix());
        viewAdapter.refreshBrick(board.getViewData());
    }

    public ScoreEvaluator getScoreEvaluator() {
        return scoreEvaluator;
    }

    public ViewData getViewData() {
        return board.getViewData();
    }

    public GameViewAdapter getViewAdapter() {
        return viewAdapter;
    }
}
