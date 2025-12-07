package com.comp2042.game.bricks;

/**
 * A special brick type used in challenge level 3.
 * <p>
 *     This brick consists of a single block with identifier 9.
 *     When it lands, it triggers an explosion mechanic defined in the game mode.
 * </p>
 */
public class BombBrick extends AbstractBrick {
    public BombBrick(){
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 9, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });

    }
}
