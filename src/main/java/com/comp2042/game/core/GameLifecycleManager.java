package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

import java.awt.*;
import java.util.ArrayList;


public class GameLifecycleManager {

    private final Board board;
    private final ScoreEvaluator scoreEvaluator;
    private final GameViewAdapter viewAdapter;
    private final ExplosionHandler explosionHandler;
    private final LineClearProcessor lineClearProcessor;

    public GameLifecycleManager(Board board, ScoreEvaluator scoreEvaluator, GameViewAdapter viewAdapter) {
        this.board = board;
        this.scoreEvaluator = scoreEvaluator;
        this.viewAdapter = viewAdapter;
        this.explosionHandler = new ExplosionHandler();
        this.lineClearProcessor = new LineClearProcessor();
    }

    public DownData processTurnEnd(GameMode mode) {
        board.mergeBrickToBackground();
        viewAdapter.refreshGameBackground(board.getBoardMatrix());
        viewAdapter.refreshBrick(board.getViewData());
        viewAdapter.onBrickLanded();

        Runnable spawnNextBrick = () -> {
            if (board.createNewBrick()) {
                viewAdapter.gameOver();
            } else {
                viewAdapter.refreshBrick(board.getViewData());
            }
        };

        Runnable afterExplosionLogic = () ->
                lineClearProcessor.processLineClears(board, scoreEvaluator, viewAdapter, spawnNextBrick);

        explosionHandler.handleExplosion(mode, board, viewAdapter, afterExplosionLogic);

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
