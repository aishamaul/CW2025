package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameViewAdapter;
import com.comp2042.util.audio.SoundManager;

import java.util.Collections;
import java.util.List;

import java.awt.*;

/**
 * Handles the execution of explosion events on the board.
 * <p>
 *     This class coordinates checking for explosion conditions via the {@link GameMode},
 *     triggering sound effects, notifying the view to play animations, and executing post-explosion logic.
 * </p>
 */
public class ExplosionHandler {

    /**
     * Checks for and processes any explosion triggered by the latest brick placement.
     * @param mode The current {@link GameMode}, which defines  explosion rules.
     * @param board The game board model.
     * @param viewAdapter The adapter to trigger UI updates and animations.
     * @param afterExplosionLogic A {@link}to execute after the explosion animation finishes.
     */
    public void handleExplosion (GameMode mode, Board board, GameViewAdapter viewAdapter,  Runnable afterExplosionLogic){

        List<Point> explodedPoints = (mode != null) ? mode.onBrickMerged(board) : Collections.emptyList();

        if (!explodedPoints.isEmpty()) {
            SoundManager.getInstance().playExplosionSound();
            viewAdapter.onExplosion(explodedPoints, () ->{
                // refresh background to show holes made by bomb before checking lines
                viewAdapter.refreshGameBackground(board.getBoardMatrix());
                afterExplosionLogic.run();
            });
        } else{
            afterExplosionLogic.run();
        }
    }
}
