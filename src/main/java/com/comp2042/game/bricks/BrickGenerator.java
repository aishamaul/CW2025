package com.comp2042.game.bricks;

import java.util.List;

public interface BrickGenerator {

    Brick getBrick();

    List<Brick> getPeekNextBricks(int count);

    void returnBrick(Brick brick);
}
