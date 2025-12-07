package com.comp2042.game.core;

import com.comp2042.game.config.GameConfig;
import com.comp2042.game.scoring.ScoreEvaluator;
import com.comp2042.ui.view.GameView;
import com.comp2042.ui.view.GameViewAdapter;

/**
 * A utility class responsible for starting the game core components.
 * <p>
 * It instantiates the Board, ScoreEvaluator, LifecycleManager, and Controller,
 * and wires them together with GameView.
 * </p>
 */
public class GameInitializer {

    public GameInitializer(GameView view){
        Board board = new SimpleBoard(GameConfig.BOARD_WIDTH, GameConfig.BOARD_HEIGHT);
        ScoreEvaluator scoreEvaluator = new ScoreEvaluator();
        GameViewAdapter viewAdapter = new GameViewAdapter(view);
        GameLifecycleManager lifecycleManager = new GameLifecycleManager(board, scoreEvaluator, viewAdapter);
        GameController controller = new GameController(board, lifecycleManager);

        board.createNewBrick();

        viewAdapter.view.setEventListener(controller);
        viewAdapter.initializeView(board.getBoardMatrix(), lifecycleManager.getViewData());
        viewAdapter.bindScore(board.getScore().scoreProperty());
        viewAdapter.bindLines(board.getScore().linesProperty());
    }
}
