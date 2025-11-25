package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import com.comp2042.game.scoring.RowScoreCalculator;
import com.comp2042.game.scoring.Score;
import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.bricks.RandomBrickGenerator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of the Board interface
 * Manages the 10x25 grid, handles the lifecycle of the active brick (spawning, moving, locking) and delegates scoring and rotation logic to helper classes
 */
public class SimpleBoard implements Board {

    private final BoardGrid grid;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private Point currentOffset;
    private final Score score;
    private final RowScoreCalculator scoreCalculator;

    public SimpleBoard(int width, int height) {
        this.grid = new BoardGrid(width, height);
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();
        this.scoreCalculator = new RowScoreCalculator();
    }

    private boolean tryMove(int dX, int dY) {
        int  targetX = (int) currentOffset.getX() + dX;
        int targetY = (int) currentOffset.getY() + dY;

        if(!grid.intersects(brickRotator.getCurrentShape(), targetX, targetY)){
            currentOffset.translate(dX, dY);
            return true;
        }
        return false;

    }

    private int calculateGhostY(){
        int currentX = (int) currentOffset.getX();
        int ghostY = (int) currentOffset.getY();
        int[][] shape = brickRotator.getCurrentShape();

        //simulate dropping until collision
        while(!grid.intersects(shape, currentX, ghostY + 1)){
            ghostY++;
        }
        return ghostY;
    }

    @Override
    public boolean moveBrickDown() {
        return tryMove(0, 1);
    }


    @Override
    public boolean moveBrickLeft() {
        return tryMove(-1, 0);
    }

    @Override
    public boolean moveBrickRight() {
        return tryMove(1, 0);
    }

    @Override
    public int dropBrick(){
        int startY = (int) currentOffset.getY();

        while (tryMove(0,1)) {
            //loop body empty, tryMove updates state
        }
        return(int) currentOffset.getY() - startY;

    }

    @Override
    public boolean rotateLeftBrick() {
        return brickRotator.tryRotate(grid, currentOffset);
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(GameConfig.SPAWN_X, GameConfig.SPAWN_Y);
        return grid.intersects(brickRotator.getCurrentShape(), (int)currentOffset.getX(), (int)currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return grid.getMatrix();
    }

    @Override
    public ViewData getViewData() {
        List<Brick> nextBricks = brickGenerator.getPeekNextBricks(3);
        List<int[][]> nextShapes = new ArrayList<>();
        for (Brick b:nextBricks){
            nextShapes.add(b.getShapeMatrix().get(0));
        }

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                calculateGhostY(),
                nextShapes
        );
    }

    @Override
    public void mergeBrickToBackground() {
        grid.merge(brickRotator.getCurrentShape(), (int)currentOffset.getX(), (int)currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        var clearingOutput = grid.clearRows();
        int linesRemoved = clearingOutput.getLinesRemoved();
        int scoreBonus = scoreCalculator.calculateScoreBonus(linesRemoved);
        return new ClearRow(linesRemoved, clearingOutput.getNewMatrix(), scoreBonus);

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        grid.clear();
        score.reset();
        createNewBrick();
    }

}
