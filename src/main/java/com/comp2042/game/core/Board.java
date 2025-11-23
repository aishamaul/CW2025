package com.comp2042.game.core;

import com.comp2042.game.scoring.Score;
import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;

public interface Board {

    /**
     * Moves the current brick down by one unit.
     * @return true if the move was successful, false if the brick cannot move down further.
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left by one unit.
     * @return true if the move was successful, false if the brick cannot move left further.
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right by one unit.
     * @return true if the move was successful, false if the brick cannot move right further.
     */
    boolean moveBrickRight();

    /**
     * Drops the current brick to the lowest possible position.
     * @return the number of rows the brick has dropped.
     */
    int dropBrick();

    /**
     * Rotates the current brick to the left  by 90 degrees (counter-clockwise).
     * @return true if rotation was successful
     */
    boolean rotateLeftBrick();

    /**
     * Spawns a new brick at the top of the board.
     * @return true if the new brick collides immediately (Game Over condition).
     */
    boolean createNewBrick();

    /**
     * Retrieves the current state of the static board grid
     * @return a 2D integer array representing the locked blocks on the board
     */
    int[][] getBoardMatrix();

    /**
     * Returns a snapshot of data needed for rendering the view
     * @return A ViewData object containing the brick positions and shapes
     */
    ViewData getViewData();

    /**
     * Locks the current active brick into the background grid
     * Should be called when the brick can no longer move down
     */
    void mergeBrickToBackground();

    /**
     * Checks for and clears any completed rows on the board
     * @return A ClearRow object containing information about cleared rows
     */
    ClearRow clearRows();

    /**
     * Gets the current score of object
     * @return the Score object
     */
    Score getScore();

    /**
     * Resets the board to start a new game
     */
    void newGame();
}
