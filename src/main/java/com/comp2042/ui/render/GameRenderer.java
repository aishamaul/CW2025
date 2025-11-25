package com.comp2042.ui.render;

import com.comp2042.game.config.GameConfig;
import com.comp2042.model.ViewData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GameRenderer {

    private final BorderPane gameBoard;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane ghostPanel;
    private final List<GridPane> nextBrickGrids;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    private final BrickColor brickColor;

    private double gridOriginX;
    private double gridOriginY;


    public GameRenderer(BorderPane gameBoard, GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel, List<GridPane> nextBrickGrids) {
        this.gameBoard = gameBoard;
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.nextBrickGrids = nextBrickGrids;
        this.brickColor = new BrickColor();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        this.gridOriginX = gameBoard.getLayoutX() + gamePanel.getLayoutX();
        this.gridOriginY = gameBoard.getLayoutY() + gamePanel.getLayoutY();

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

        //position of real brick
        brickPanel.setLayoutX(gridOriginX + gamePanel.getLayoutX() + (brick.getxPosition() * (GameConfig.BRICK_SIZE + brickPanel.getHgap())));
        brickPanel.setLayoutY(gridOriginY + gamePanel.getLayoutY() + (brick.getyPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + brickPanel.getVgap()));
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }

        //position of ghost brick
        ghostPanel.setLayoutX(gridOriginX + gamePanel.getLayoutX() + (brick.getxPosition() * (GameConfig.BRICK_SIZE + ghostPanel.getHgap())));
        ghostPanel.setLayoutY(gridOriginY + gamePanel.getLayoutY() + (brick.getGhostYPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + ghostPanel.getVgap()));

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for(int j = 0; j < brick.getBrickData()[i].length; j++){
                int colorIndex = brick.getBrickData()[i][j];

                setRectangleData(colorIndex, rectangles[i][j]);
                setRectangleData(colorIndex, ghostRectangles[i][j]);

            }
        }
        refreshNextBricks(brick.getNextBricksData());
    }

    private void refreshNextBricks(List<int[][]> nextBricksData){
       //iterate through available data
        for(int i = 0; i < nextBricksData.size() && i  < nextBrickGrids.size(); i++){
           int[][] brickMatrix = nextBricksData.get(i);
           GridPane targetGrid = nextBrickGrids.get(i);

           // clear previous shape
            targetGrid.getChildren().clear();

            //draw new shape
            for (int row = 0; row < brickMatrix.length; row++){
                for (int col = 0; col < brickMatrix[row].length; col++){
                    int colorCode = brickMatrix[row][col];
                    if (colorCode != 0){
                        Rectangle rectangle = new Rectangle(18,18);
                        rectangle.setFill(brickColor.getFillColor(colorCode));
                        targetGrid.add(rectangle, col, row);
                    }
                }
            }
       }

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

}