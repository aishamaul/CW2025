package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import com.comp2042.game.scoring.RowScoreCalculator;
import com.comp2042.game.scoring.Score;
import com.comp2042.game.bricks.Brick;
import com.comp2042.game.bricks.BrickGenerator;
import com.comp2042.game.bricks.RandomBrickGenerator;
import com.comp2042.model.ClearRow;
import com.comp2042.model.ViewData;
import com.comp2042.util.ExplosionManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of the Board interface
 * Manages the 10x25 grid, handles the lifecycle of the active brick (spawning, moving, locking) and delegates scoring and rotation logic to helper classes
 */
public class SimpleBoard implements Board {

    private final BoardGrid grid;
    private BrickGenerator brickGenerator;
    private final ActivePiece activePiece;
    private final Score score;
    private final RowScoreCalculator scoreCalculator;
    private Brick heldBrick;
    private final ExplosionManager explosionManager;
    private boolean isBrickActive = false;

    private final HoldManager holdManager; // New Dependency
    public SimpleBoard(int width, int height) {
        this.grid = new BoardGrid(width, height);
        this.brickGenerator = new RandomBrickGenerator();
        this.activePiece = new ActivePiece();
        this.score = new Score();
        this.scoreCalculator = new RowScoreCalculator();
        this.explosionManager = new ExplosionManager();
        this.holdManager = new HoldManager();

    }


    @Override
    public boolean moveBrickDown() {
        return activePiece.move(0, 1, grid);
    }

    @Override
    public boolean canMoveDown(){ return activePiece.move(0, 1, grid); }


    @Override
    public boolean moveBrickLeft() {
        return activePiece.move(-1, 0, grid);
    }

    @Override
    public boolean moveBrickRight() {
        return activePiece.move(1, 0, grid);
    }

    @Override
    public int dropBrick(){
        int startY = (int) activePiece.getY();

        while (activePiece.move(0,1, grid)) {
            //loop body empty, tryMove updates state
        }
        return(int) activePiece.getY() - startY;

    }

    @Override
    public boolean rotateLeftBrick() {
        return activePiece.rotate(grid);
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        activePiece.spawn(currentBrick, GameConfig.SPAWN_X, GameConfig.SPAWN_Y);
        isBrickActive = true;
        return grid.intersects(activePiece.getShape(), activePiece.getX(), activePiece.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return grid.getMatrix();
    }

    @Override
    public void holdBrick(){
        holdManager.processHold(activePiece, brickGenerator, this::createNewBrick);

        isBrickActive = true;
    }

    @Override
    public ViewData getViewData() {
        List<Brick> nextBricks = brickGenerator.getPeekNextBricks(3);
        List<int[][]> nextShapes = new ArrayList<>();
        for (Brick b:nextBricks){
            nextShapes.add(b.getShapeMatrix().get(0));
        }

        //get held brick shape
        Brick heldBrick = holdManager.getHeldBrick();
        int [][] holdShape = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;

        int[][] currentShape = activePiece.getShape();
        int ghostY = activePiece.getY();

        if (isBrickActive) {
            ghostY = activePiece.calculateGhostY(grid);
        } else {
            currentShape = new int[currentShape.length][currentShape[0].length];
        }

        return new ViewData(
                currentShape,
                activePiece.getX(),
                activePiece.getY(),
                activePiece.calculateGhostY(grid),
                nextShapes,
                holdShape
        );
    }

    @Override
    public void mergeBrickToBackground() {
        grid.merge(activePiece.getShape(), activePiece.getX(), activePiece.getY());
        isBrickActive = false;
    }

    @Override
    public ClearRow clearRows() {
        var clearingOutput = grid.clearRows();
        int linesRemoved = clearingOutput.getLinesRemoved();
        int scoreBonus = scoreCalculator.calculateScoreBonus(linesRemoved);
        return new ClearRow(linesRemoved, clearingOutput.getNewMatrix(), scoreBonus, clearingOutput.getClearedIndices());

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        grid.clear();
        score.reset();
        holdManager.reset(); //reset hold
        createNewBrick();
    }

    @Override
    public boolean addGarbageLine(){
        return grid.addGarbageLine();
    }

    @Override
    public boolean insertRowAtBottom(int[] row){
        return grid.insertRowAtBottom(row);
    }

    @Override
    public void setBrickGenerator(BrickGenerator generator) {
        this.brickGenerator = generator;
    }

    @Override
    public Point getCurrentOffset(){
        return new Point(activePiece.getX(), activePiece.getY());
    }

    @Override
    public Brick getCurrentBrick() {
        return activePiece.getBrick();
    }

    @Override
    public List<Point> explode(int x, int y, int radius) {
        return explosionManager.processExplosion(grid, x, y, radius);
    }

}
