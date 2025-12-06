package com.comp2042.game.bricks;


public class Level3BrickGenerator extends RandomBrickGenerator {

    public Level3BrickGenerator () {
        super();

        // add the bomb brick to the pool
        brickList.add(new BombBrick());

    }



}
