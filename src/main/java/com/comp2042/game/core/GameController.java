package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import com.comp2042.game.mode.GameMode;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;
import com.comp2042.util.audio.SoundManager;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

/**
 * The GameController class serves as the central hub for managing game interactions.
 * It processes user input events, updates the game board, and coordinates with the game lifecycle manager
 * to handle scoring and view updates.
 */
public class GameController implements InputEventListener {

    private final Board board;
    private final GameLifecycleManager lifecycleManager;
    private GameMode currentMode;

    // timer to handle the "Lock Delay"
    private final Timeline lockTimer;

    public GameController(Board board, GameLifecycleManager lifecycleManager) {
        this.board = board;
        this.lifecycleManager = lifecycleManager;

        this.lockTimer = new Timeline(new KeyFrame(Duration.millis(GameConfig.LOCK_DELAY_MS), e -> {
            lifecycleManager.processTurnEnd(currentMode);
        }));
        this.lockTimer.setCycleCount(1);

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
            if(lockTimer.getStatus() == Animation.Status.RUNNING){
                lockTimer.stop();
            }

            lifecycleManager.getScoreEvaluator().scoreMovement(event, board.getScore());
            lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
            return new DownData(null, lifecycleManager.getViewData());
        }else{

            if (lockTimer.getStatus() != Animation.Status.RUNNING) {
                SoundManager.getInstance().playLandSound();
                lockTimer.playFromStart();
            }
            return new DownData(null, lifecycleManager.getViewData());
        }
    }

    /**
     * Handles the 'Drop' event (Hard Drop).
     * Instantly drops the brick to the bottom and locks it.
     * @param event The move event context.
     * @return {@link DownData} containing the results of the turn end (e.g., cleared lines).
     */
    @Override
    public DownData onDropEvent(MoveEvent event){
        if(lockTimer.getStatus() == Animation.Status.RUNNING){
            lockTimer.stop();
        }
        SoundManager.getInstance().playHardDropSound();
        int rowsDropped = board.dropBrick();
        lifecycleManager.getScoreEvaluator().scoreDrop(rowsDropped, board.getScore());
        lifecycleManager.getViewAdapter().onHardDrop(board.getViewData());
        return lifecycleManager.processTurnEnd(currentMode);
    }

    /**
     * Handles the 'Left' movement event.
     *
     * @param event The move event context.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (board.moveBrickLeft()) {
            SoundManager.getInstance().playMoveSound();
        }        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    /**
     * Handles the 'Right' movement event.
     *
     * @param event The move event context.
     * @return The updated {@link ViewData}.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        if (board.moveBrickRight()) {
            SoundManager.getInstance().playMoveSound();
        }        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    /**
     * Handles the 'Rotate' event.
     *
     * @param event The move event context.
     * @return The updated {@link ViewData} reflecting the new rotation.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        lifecycleManager.getViewAdapter().refreshBrick((lifecycleManager.getViewData()));
        return lifecycleManager.getViewData();
    }

    /**
     * Resets the game state to start a new session.
     */
    @Override
    public void createNewGame() {
        if (lockTimer.getStatus() == Animation.Status.RUNNING) {
            lockTimer.stop();
        }
        lifecycleManager.handleNewGame(currentMode);
    }

    /**
     * Handles the 'Hold' event to swap the active brick with the held brick.
     *
     * @param event The move event context.
     * @return The updated {@link ViewData} with the new active brick.
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event){
        SoundManager.getInstance().playHoldSound();
        board.holdBrick();

        if (lockTimer.getStatus() == Animation.Status.RUNNING) {
            lockTimer.stop();
        }

        lifecycleManager.getViewAdapter().refreshBrick(lifecycleManager.getViewData());
        return lifecycleManager.getViewData();
    }

    /**
     * Process a game tick (frame update).
     * Delegates to the {@link GameMode} to determine if gravity should be applied.
     *
     * @param mode The current game mode.
     * @return true if gravity was applied, false otherwise.
     */
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

    /**
     * Sets the current game mode strategy.
     *
     * @param mode The {@link GameMode} to use.
     */
    @Override
    public void setGameMode(GameMode mode) {
        this.currentMode = mode;
    }
}
