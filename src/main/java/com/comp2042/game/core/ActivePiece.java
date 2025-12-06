package com.comp2042.game.core;

import com.comp2042.game.bricks.Brick;

import java.awt.*;

public class ActivePiece {

    private final BrickRotator rotator;
    private Point position;

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

    public int[][] getShape(){
        return rotator.getCurrentShape();
    }

    public Brick getBrick(){
        return rotator.getBrick();
    }

    public int getX(){
        return (int)position.getX();
    }

    public int getY(){
        return (int)position.getY();
    }

    public int calculateGhostY(BoardGrid grid){
        return grid.calculateDropPosition(getShape(), getX(), getY());
    }
}
