package com.comp2042.ui.render;

import com.comp2042.game.config.GameConfig;
import com.comp2042.model.ViewData;
import javafx.animation.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class GameRenderer {

    private final BorderPane gameBoard;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private final List<GridPane> nextBrickGrids;
    private final GridPane holdBrickGrid;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    private final BrickColor brickColor;
    private final GridAnimator gridAnimator;

    private double gridOriginX;
    private double gridOriginY;


    public GameRenderer(BorderPane gameBoard, GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel,
                        List<GridPane> nextBrickGrids, GridPane holdBrickGrid) {
        this.gameBoard = gameBoard;
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.nextBrickGrids = nextBrickGrids;
        this.holdBrickGrid = holdBrickGrid;
        this.brickColor = new BrickColor();
        this.gridAnimator = new GridAnimator();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = GameConfig.HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                rectangle.setFill(brickColor.getFillColor(0));
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        //initialize ghost brick rectangles
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {

                //real brick
                Rectangle rectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                rectangle.setFill(brickColor.getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);

                //ghost brick
                Rectangle ghostRectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                ghostRectangle.setFill(brickColor.getFillColor(brick.getBrickData()[i][j]));

                ghostRectangle.setOpacity(0.2);

                ghostRectangles[i][j] = ghostRectangle;
                ghostPanel.add(ghostRectangle, j, i);
            }
        }
        refreshBrick(brick);
    }

    public void refreshBrick(ViewData brick) {

        updateActiveBrickVisuals(brick);
        updateGhostBrickVisuals(brick);

        refreshNextBricks(brick.getNextBricksData());
        refreshHoldBrick(brick.getHoldBrickData());
    }

    private void updateActiveBrickVisuals(ViewData brick) {
        double xPos = gridOriginX + (brick.getxPosition() * (GameConfig.BRICK_SIZE + brickPanel.getHgap())) + GameConfig.MANUAL_X_OFFSET;
        double yPos = gridOriginY + (brick.getyPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + brickPanel.getVgap()) + GameConfig.MANUAL_Y_OFFSET;

        brickPanel.setLayoutX(xPos);
        brickPanel.setLayoutY(yPos);

        updateRectangles(brick.getBrickData(), rectangles);
    }

    private void updateGhostBrickVisuals(ViewData brick) {
        double xPos = gridOriginX + (brick.getxPosition() * (GameConfig.BRICK_SIZE + brickPanel.getHgap())) + GameConfig.MANUAL_X_OFFSET;
        double yPos = gridOriginY + (brick.getGhostYPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + ghostPanel.getVgap()) + GameConfig.MANUAL_Y_OFFSET;

        ghostPanel.setLayoutX(xPos);
        ghostPanel.setLayoutY(yPos);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for(int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], ghostRectangles[i][j]);
            }
        }
    }

    private void updateRectangles(int[][] data, Rectangle[][] targets) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                setRectangleData(data[i][j], targets[i][j]);
            }
        }
    }

    private void drawMatrixToGrid(GridPane targetGrid, int[][] matrix){
        targetGrid.getChildren().clear();
        if(matrix==null) return;

        for(int row = 0; row < matrix.length; row++){
            for (int col = 0; col < matrix[row].length; col++){
                int colorCode = matrix[row][col];
                if (colorCode != 0){
                    Rectangle rectangle = new Rectangle(18, 18);
                    rectangle.setFill(brickColor.getFillColor(colorCode));
                    targetGrid.add(rectangle, col, row);
                }
            }
        }
    }


    private void refreshNextBricks(List<int[][]> nextBricksData){
       //iterate through available data
        for(int i = 0; i < nextBricksData.size() && i  < nextBrickGrids.size(); i++){
           drawMatrixToGrid(nextBrickGrids.get(i), nextBricksData.get(i));
       }
    }

    private void refreshHoldBrick(int[][] holdBrickData){
        drawMatrixToGrid(holdBrickGrid, holdBrickData);

    }


    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }


    private void setRectangleData(int color, Rectangle rectangle) {

        rectangle.setFill(brickColor.getFillColor(color));
    }


    public void animateClear(List<Integer> clearedIndices, Runnable onFinished){
        gridAnimator.animateClear(this.displayMatrix, clearedIndices, onFinished);
    }

    public void animateExplosion(List<java.awt.Point> explodedPoints, Runnable onFinished){
        gridAnimator.animateExplosion(this.displayMatrix, explodedPoints, onFinished);
    }

}