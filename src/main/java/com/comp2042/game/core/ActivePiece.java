package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;

import java.awt.*;

/**
 * Represents the currently active brick on the game board.
 * <p>
 * This class maintains the state of the active piece, including its current position,
 * its underlying {@link Brick} type, and its rotation handler. It facilitates movement
 * and rotation operations relative to the board grid.
 * </p>
 */
public class ActivePiece {

    private final BrickRotator rotator;
    private Point position;

    /**
     * Constructs a new ActivePiece with a default position (0,0) and rotation.
     */
    public ActivePiece(){
        this.rotator = new BrickRotator();
        this.position = new Point(0,0);
    }

    /**
     * Spawns a new brick at the specified coordinates.
     */

    public void spawn (Brick brick, int x, int y){
        rotator.setBrick(brick);

        // create a new point to ensure fresh state
        this.position = new Point(x, y);
    }

    /**
     *  Attempts to move the piece.
     * @return true if the move was successful (no collision).
     */
    public boolean move(int dx, int dy, BoardGrid grid){
        int targetX = (int) position.getX() + dx;
        int targetY = (int) position.getY() + dy;

        if (!grid.intersects(rotator.getCurrentShape(), targetX, targetY)){
            position.translate(dx, dy);
            return true;
        }
        return false;
    }

    /**
     * Attempts to rotate the piece.
     * @return true if rotation was successful.
     */
    public boolean rotate(BoardGrid grid){
        return rotator.tryRotate(grid, position);
    }

    /**
     * Gets the matrix shape of the current rotation state.
     * @return A 2D integer array of the shape.
     */
    public int[][] getShape(){
        return rotator.getCurrentShape();
    }

    /**
     * Gets the underlying brick type.
     * @return The {@link Brick} instance.
     */
    public Brick getBrick(){
        return rotator.getBrick();
    }

    /**
     * Gets the current x-coordinate.
     * @return The x position.
     */
    public int getX(){
        return (int)position.getX();
    }

    /**
     * Gets the current y-coordinate.
     * @return The y position.
     */
    public int getY(){
        return (int)position.getY();
    }

    /**
     * Calculates the lowest valid y-position for the ghost piece.
     *
     * @param grid The board grid to check against.
     * @return The y-coordinate where the piece would land.
     */
    public int calculateGhostY(BoardGrid grid){
        return grid.calculateDropPosition(getShape(), getX(), getY());
    }
}
