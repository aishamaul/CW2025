package com.comp2042.game.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    protected final List<Brick> brickList;

    protected final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        fillQueue();

    }

    protected void fillQueue(){
        while (nextBricks.size()<4){
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
            if(nextBricks.isEmpty()){
                fillQueue();
            }
            return nextBricks.peek();
    }

    @Override
    public List<Brick> getPeekNextBricks(int count) {
        while (nextBricks.size()<count){
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }

        return new ArrayList<>(nextBricks).subList(0, count);
    }

    @Override
    public void returnBrick(Brick brick) {
        nextBricks.addFirst(brick);
    }
}
