package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.config.GameConfig;

public class HoldManager {

    private Brick heldBrick;

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

    public Brick getHeldBrick() {
        return heldBrick;
    }

    public void reset() {
        heldBrick = null;
    }
}
