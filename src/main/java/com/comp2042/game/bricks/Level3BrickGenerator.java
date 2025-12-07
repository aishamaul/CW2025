package com.comp2042.game.bricks;

/**
 * A specialized brick generator for challenge level 3.
 * <p>
 *     Extends the standard random generator by including the {@link BombBrick}
 *     in the pool of available pieces.
 * </p>
 */
public class Level3BrickGenerator extends RandomBrickGenerator {

    public Level3BrickGenerator () {
        super();

        // add the bomb brick to the pool
        brickList.add(new BombBrick());

    }



}
