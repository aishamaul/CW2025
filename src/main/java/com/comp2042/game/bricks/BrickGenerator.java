package com.comp2042.game.bricks;

import java.util.List;

/**
 * Interface defining the strategy for generating new bricks.
 * <p>
 *     Implementations can define different randomizers
 *     or inject special bricks for specific game modes.
 * </p>
 */
public interface BrickGenerator {

    /**
     * Retrieves the next brick from the queue and generates a new one to refill the queue.
     *
     * @return A {@link Brick} instance to be used as the active piece.
     */
    Brick getBrick();

    /**
     * Peeks at the upcoming bricks without removing them from the queue.
     * Useful for displaying the next piece preview UI
     *
     * @param count The number of future bricks to retrieve.
     * @return A list of the next {@link Brick} objects.
     */
    List<Brick> getPeekNextBricks(int count);

    /**
     * Returns a brick to the generator
     * @param brick The brick being returned to the queue or storage.
     */
    void returnBrick(Brick brick);
}
