package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

import java.awt.*;
import java.util.ArrayList;


/**
 * Manages the high level lifecycle of game events, specifically the transition between turns.
 * <p>
 *     This class coordinates the sequence of events that occur when a brick lands:
 *     locking the brick, checking for explosions, processing line clears, updating the score,
 *     and spawning the next brick (or triggering game over).
 * </p>
 */
public class GameLifecycleManager {

    private final Board board;
    private final ScoreEvaluator scoreEvaluator;
    private final GameViewAdapter viewAdapter;
    private final ExplosionHandler explosionHandler;
    private final LineClearProcessor lineClearProcessor;

    /**
     * Constructs a new lifecycle manager.
     *
     * @param board The game board model.
     * @param scoreEvaluator The rule engine for calculating scores.
     * @param viewAdapter The adapter for communicating updates to the UI.
     */
    public GameLifecycleManager(Board board, ScoreEvaluator scoreEvaluator, GameViewAdapter viewAdapter) {
        this.board = board;
        this.scoreEvaluator = scoreEvaluator;
        this.viewAdapter = viewAdapter;
        this.explosionHandler = new ExplosionHandler();
        this.lineClearProcessor = new LineClearProcessor();
    }

    /**
     * Executes the logic for ending a turn (when a brick cannot move further down).
     * <p>
     *     The sequence is:
     *     1. Merge brick to background.
     *     2. Handle specific game mode events.
     *     3. Process line clears.
     *     4. Spawn the next brick.
     *     5. Check for game over.
     * </p>
     * @param mode The current game mode (used for checking special brick effects).
     * @return A {@link DownData} object representing the immediate state update.
     */
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

    /**
     * Resets the game state for a new session.
     *
     * @param mode The game mode to initialize.
     */
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
