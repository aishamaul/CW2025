package com.comp2042.game.core;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import com.comp2042.ui.view.GameView;

/**
 * The GameController class serves as the central hub for managing game interactions.
 * It processes user input events, updates the game board, and coordinates with the game lifecycle manager
 * to handle scoring and view updates.
 */
public class GameController implements InputEventListener {

    private final Board board;
    private final GameLifecycleManager lifecycleManager;
    private GameMode currentMode;

    public GameController(Board board, GameLifecycleManager lifecycleManager) {
        this.board = board;
        this.lifecycleManager = lifecycleManager;

    }

    /**
     * Handles the specific 'Down' movement event.
     * If the brick cannot move down, this method triggers the turn end logic (locking the bricks).
     *
     * @param event The move event context
     * @return DownData containing the view update information or row clearing results
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        if (board.moveBrickDown()){
            lifecycleManager.getScoreEvaluator().scoreMovement(event, board.getScore());
            lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
            return new DownData(null, lifecycleManager.getViewData());
        }else{
            return lifecycleManager.processTurnEnd(currentMode);
        }
    }

    @Override
    public DownData onDropEvent(MoveEvent event){
        int rowsDropped = board.dropBrick();
        lifecycleManager.getScoreEvaluator().scoreDrop(rowsDropped, board.getScore());
        return lifecycleManager.processTurnEnd(currentMode);
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        lifecycleManager.getViewAdapter().refreshBrick((lifecycleManager.getViewData()));
        return lifecycleManager.getViewData();
    }


    @Override
    public void createNewGame() {
        lifecycleManager.handleNewGame(currentMode);
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event){
        board.holdBrick();
        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    @Override
    public boolean onGameTick(GameMode mode) {
        // default behavior (practice/classic), always apply gravity
        if (mode == null) {
            return true;
        }

        // challenge behavior, ask the level logic
        boolean applyGravity = mode.onGameTick(board);

        lifecycleManager.getViewAdapter().refreshGameBackground(board.getBoardMatrix());
        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());

        return applyGravity;
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.currentMode = mode;
    }
}
