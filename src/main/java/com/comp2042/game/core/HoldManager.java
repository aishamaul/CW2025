package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.config.GameConfig;

/**
 * Manages the hold mechanic, allowing the player to store a brick for later use.
 */
public class HoldManager {

    private Brick heldBrick;

    /**
     * Swaps the currently falling active piece with the held brick
     * <p>
     *     Tf no brick is currently held, the active piece is moved to the hold slot,
     *     and a new brick is spawned
     * </p>
     * @param activePiece The current active piece wrapper.
     * @param brickGenerator The generator used to return the brick or spawn a new one.
     * @param onSpawnNeeded Callback to execute if a new brick needs to be spawned (hold was empty).
     */
    public void processHold(ActivePiece activePiece, BrickGenerator brickGenerator, Runnable onSpawnNeeded) {
        Brick currentBrick = activePiece.getBrick();

        if (heldBrick == null) {
            // case 1: held is empty
            // put current in hold
            heldBrick = currentBrick;
            // spawn next brick
            onSpawnNeeded.run();
        } else {
            // case 2: hold has a brick
            // current changes to brick in hold, hold becomes empty
            Brick brickFromHold = heldBrick;
            heldBrick = null;

            // push the current falling brick to the generator
            brickGenerator.returnBrick(currentBrick);

            // set the current brick to the one that was in hold
            activePiece.spawn(brickFromHold, GameConfig.SPAWN_X, GameConfig.SPAWN_Y);
        }
    }

    /**
     * Retrieves the brick currently in the hold slot.
     * @return The held {@link Brick}, or null if the hold slot is empty.
     */
    public Brick getHeldBrick() {
        return heldBrick;
    }

    /**
     * Clears the held brick.
     */
    public void reset() {
        heldBrick = null;
    }
}
