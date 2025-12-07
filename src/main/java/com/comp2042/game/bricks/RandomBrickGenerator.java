package com.comp2042.game.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A standard implementation of {@link BrickGenerator} that provides bricks in a random order.
 * <p>
 *     This generator maintains a queue of upcoming bricks to allow the player to see
 *     what pieces are coming next. It refills the same queue from a predefined list of standard bricks.
 * </p>
 */
public class RandomBrickGenerator implements BrickGenerator {

    protected final List<Brick> brickList;

    protected final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Constructs a new RandomBrickGenerator.
     * <p>
     * Initializes the list of standard Tetrominoes (I, J, L, O, S, T, Z) and
     * pre-fills the queue.
     * </p>
     */
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

    /**
     * Refills the internal queue of bricks until it contains at least 4 items.
     * <p>
     *     Randomly selects bricks from the {@code brickList} to add to the queue.
     * </p>
     */
    protected void fillQueue(){
        while (nextBricks.size()<4){
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));

        }
    }

    /**
     * Retrieves the next brick from the queue and generates a new one to refill the queue.
     * @return A {@link Brick} instance to be used as the active piece.
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            fillQueue();
        }
        Brick brick = nextBricks.poll();
        fillQueue();
        return brick;
    }

    /**
     * Peeks at the upcoming bricks without removing them from the queue.
     * @param count The number of future bricks to retrieve.
     * @return A list of the next {@link Brick objects}.
     */
    @Override
    public List<Brick> getPeekNextBricks(int count) {
        while (nextBricks.size()<count){
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }

        return new ArrayList<>(nextBricks).subList(0, count);
    }

    /**
     * Returns a brick to the front of the generator's queue.
     * Used when swapping the active bricks with the held brick.
     * @param brick The brick being returned to the queue or storage.
     */
    @Override
    public void returnBrick(Brick brick) {
        nextBricks.addFirst(brick);
    }
}
