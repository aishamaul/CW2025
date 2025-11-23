package com.comp2042.game.config;

public class GameConfig {
    //board dimensions
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 25;

    //spawning
    public static final int SPAWN_X = 4;
    public static final int SPAWN_Y = 1;

    //game loop
    public static final long GAME_LOOP_INTERVAL_MS = 400;

    //rendering
    public static final int BRICK_SIZE = 20;
    public static final int HIDDEN_ROWS = 2;

    private GameConfig(){}
}
