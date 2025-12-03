package com.comp2042.game.core;

import com.comp2042.game.mode.GameMode;
import com.comp2042.ui.view.GameView;
import com.comp2042.ui.view.GameViewAdapter;

import java.util.Collections;
import java.util.List;

import java.awt.*;

public class ExplosionHandler {

    public void handleExplosion (GameMode mode, Board board, GameViewAdapter viewAdapter,  Runnable afterExplosionLogic){

        List<Point> explodedPoints = (mode != null) ? mode.onBrickMerged(board) : Collections.emptyList();

        if (!explodedPoints.isEmpty()) {
            viewAdapter.onExplosion(explodedPoints, () ->{
                // refresh background to show holes made by bomb before checking lines
                viewAdapter.refreshGameBackground(board.getBoardMatrix());
                afterExplosionLogic.run();
            });
        } else{
            afterExplosionLogic.run();
        }
    }
}
