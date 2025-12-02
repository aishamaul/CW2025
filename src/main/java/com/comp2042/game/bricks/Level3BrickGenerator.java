package com.comp2042.game.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Level3BrickGenerator extends RandomBrickGenerator {

    public Level3BrickGenerator () {
        super();

        // add the bomb brick to the pool
        brickList.add(new BombBrick());

    }



}
