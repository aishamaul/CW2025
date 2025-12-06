package com.comp2042.ui.render;

import com.comp2042.game.config.GameConfig;
import com.comp2042.model.ViewData;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Handles the rendering of the game state onto the JavaFX UI.
 * <p>
 *     This class manages the creation, updates and styling of {@link Rectangle} objects
 *     that represent the game board, the active falling brick, the ghost brick,
 *     and the next/hold brick previews. It bridges the gap between the internal
 *     data model and the visual components
 * </p>
 */

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

    private final BrickStyler brickStyler;
    private final GridAnimator gridAnimator;

    private ViewData latestBrickData;

    private double gridOriginX;
    private double gridOriginY;


    /**
     * Constructs a new GameRenderer
     *
     * @param gameBoard The main container for the game area.
     * @param gamePanel The grid representing the static background board.
     * @param brickPanel The grid overlay for the active falling brick.
     * @param ghostPanel The grid overlay for the ghost brick.
     * @param nextBrickGrids A list of grids to display upcoming bricks.
     * @param holdBrickGrid The grid to display the held brick.
     */
    public GameRenderer(BorderPane gameBoard, GridPane gamePanel, GridPane brickPanel, GridPane ghostPanel,
                        List<GridPane> nextBrickGrids, GridPane holdBrickGrid) {
        this.gameBoard = gameBoard;
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.ghostPanel = ghostPanel;
        this.nextBrickGrids = nextBrickGrids;
        this.holdBrickGrid = holdBrickGrid;
        this.brickStyler = new BrickStyler();
        this.gridAnimator = new GridAnimator();
    }

    /**
     * Initializes the visual grid of Rectangles based on the initial board state.
     * Populates the JavaFX GridPanes with Rectangle objects that will be reused for rendering.
     *
     * @param boardMatrix The initial state of the board.
     * @param brick The initial view data for the active brick.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = GameConfig.HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                setRectangleData(0, rectangle);
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
                setRectangleData(brick.getBrickData()[i][j], rectangle);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);

                //ghost brick
                Rectangle ghostRectangle = new Rectangle(GameConfig.BRICK_SIZE, GameConfig.BRICK_SIZE);
                setRectangleData(brick.getBrickData()[i][j], ghostRectangle);

                ghostRectangle.setOpacity(0.2);

                ghostRectangles[i][j] = ghostRectangle;
                ghostPanel.add(ghostRectangle, j, i);
            }
        }
        refreshBrick(brick);
    }

    /**
     * Updates the visual position and shape of the active brick, ghost brick,
     * next bricks and held brick.
     *
     * @param brick The {@link ViewData} containing the current state of the moving pieces.
     */
    public void refreshBrick(ViewData brick) {
        latestBrickData = brick;

        if (!updateGridOrigin()) {
            setBrickPanelsVisible(false);
            Platform.runLater(() -> {
                if (latestBrickData != null) {
                    refreshBrick(latestBrickData);
                }
            });
            return;
        }
        setBrickPanelsVisible(true);

        updateActiveBrickVisuals(brick);
        updateGhostBrickVisuals(brick);

        refreshNextBricks(brick.getNextBricksData());
        refreshHoldBrick(brick.getHoldBrickData());
    }

    private void updateActiveBrickVisuals(ViewData brick) {
        double xPos = gridOriginX + (brick.getxPosition() * (GameConfig.BRICK_SIZE + brickPanel.getHgap()));
        double yPos = gridOriginY + (brick.getyPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + brickPanel.getVgap());

        brickPanel.setLayoutX(xPos);
        brickPanel.setLayoutY(yPos);

        updateRectangles(brick.getBrickData(), rectangles);
    }

    private void updateGhostBrickVisuals(ViewData brick) {
        double xPos = gridOriginX + (brick.getxPosition() * (GameConfig.BRICK_SIZE + brickPanel.getHgap()));
        double yPos = gridOriginY + (brick.getGhostYPosition() - GameConfig.HIDDEN_ROWS) * (GameConfig.BRICK_SIZE + ghostPanel.getVgap());

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
                    setRectangleData(colorCode, rectangle);
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


    /**
     * Updates the static background grid to reflect the locked bricks.
     *
     * @param board The 2D array representing the locked bricks.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }


    /**
     * Applies the appropriate CSS style class to a rectangle based on the color code.
     */
    private void setRectangleData(int color, Rectangle rectangle) {

        brickStyler.applyBrickStyle(rectangle, color);
    }


    public void animateClear(List<Integer> clearedIndices, Runnable onFinished){
        gridAnimator.animateClear(this.displayMatrix, clearedIndices, onFinished);
    }

    public void animateExplosion(List<java.awt.Point> explodedPoints, Runnable onFinished){
        gridAnimator.animateExplosion(this.displayMatrix, explodedPoints, onFinished);
    }

    public void playLandingAnimation(){
        gridAnimator.animateBoardShake(this.gamePanel);
    }

    public void playHardDropParticleEffect(ViewData brick) {
        int[][] shape = brick.getBrickData();
        int brickX = brick.getxPosition();
        int brickY = brick.getyPosition();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0) {
                    int boardRow = brickY + row;
                    int boardCol = brickX + col;

                    if (boardRow < 0 || boardRow >= displayMatrix.length ||
                            boardCol < 0 || boardCol >= displayMatrix[0].length) {
                        continue;
                    }
                    Rectangle targetCell = displayMatrix[boardRow][boardCol];
                    if (targetCell == null) {
                        continue;
                    }

                    Bounds cellBounds = targetCell.getBoundsInParent();
                    gridAnimator.spawnSplashParticles(gamePanel, cellBounds.getMinX(), cellBounds.getMinY(), shape[row][col]);                }
            }
        }
    }

    private boolean updateGridOrigin(){
        if (brickPanel.getParent() == null || gamePanel.getScene() == null){
            return false;
        }


        if (displayMatrix == null || displayMatrix.length <= GameConfig.HIDDEN_ROWS
                || displayMatrix[GameConfig.HIDDEN_ROWS].length == 0) {
            return false;
        }

        Rectangle referenceCell = displayMatrix[GameConfig.HIDDEN_ROWS][0];
        if (referenceCell == null) {
            return false;
        }

        Bounds cellBounds = referenceCell.localToScene(referenceCell.getBoundsInLocal());
        Point2D parentOrigin = brickPanel.getParent().sceneToLocal(cellBounds.getMinX(), cellBounds.getMinY());
        gridOriginX = parentOrigin.getX();
        gridOriginY = parentOrigin.getY();
        return true;
    }

    private void setBrickPanelsVisible(boolean visible) {
        if (brickPanel.isVisible() == visible && ghostPanel.isVisible() == visible) {
            return;
        }

        brickPanel.setVisible(visible);
        ghostPanel.setVisible(visible);
    }
}