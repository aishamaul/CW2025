package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class GameLifecycleManager {

    private final Board board;
    private final ScoreEvaluator scoreEvaluator;
    private final GameViewAdapter viewAdapter;

    public GameLifecycleManager(Board board, ScoreEvaluator scoreEvaluator, GameViewAdapter viewAdapter) {
        this.board = board;
        this.scoreEvaluator = scoreEvaluator;
        this.viewAdapter = viewAdapter;
    }

    public DownData processTurnEnd(GameMode mode) {
        board.mergeBrickToBackground();

        // check for Bomb Logic
        List<Point> explodedPoints = new ArrayList<>();
        if (mode != null) {
            explodedPoints = mode.onBrickMerged(board);
        }

        // define logic to run AFTER explosion (Line clears & New Brick)
        Runnable afterExplosionLogic = () -> {
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
        };

        // trigger Animation or Run Immediately
        if (!explodedPoints.isEmpty()) {
            viewAdapter.onExplosion(explodedPoints, () -> {
                // refresh background to show holes made by bomb before checking lines
                viewAdapter.refreshGameBackground(board.getBoardMatrix());
                afterExplosionLogic.run();
            });
        } else {
            afterExplosionLogic.run();
        }

        // return current state immediately (Animation handles visual updates asynchronously)
        return new DownData(new ClearRow(0, board.getBoardMatrix(), 0, new ArrayList<>()), board.getViewData());
    }

    public void handleNewGame(GameMode mode) {
        board.newGame();

        if (mode != null) {
            mode.onStart(board);
        }
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
