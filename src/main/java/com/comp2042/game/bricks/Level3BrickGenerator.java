package com.comp2042.game.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Level3BrickGenerator implements BrickGenerator {
    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public Level3BrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        // add the bomb brick to the pool
        brickList.add(new BombBrick());

        fillQueue();
    }

    private void fillQueue() {
        while (nextBricks.size() < 4) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
    }

    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            fillQueue();
        }
        Brick brick = nextBricks.poll();
        fillQueue();
        return brick;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    @Override
    public List<Brick> getPeekNextBricks(int count) {
        fillQueue();
        return new ArrayList<>(nextBricks).subList(0, count);
    }

    @Override
    public void returnBrick(Brick brick) {
        nextBricks.addFirst(brick);
    }

}
