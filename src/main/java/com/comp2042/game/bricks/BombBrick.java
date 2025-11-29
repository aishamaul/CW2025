package com.comp2042.game.bricks;

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
