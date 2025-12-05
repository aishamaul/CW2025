package com.comp2042.game.mode;

import com.comp2042.game.core.Board;
import com.comp2042.game.core.GameLoopManager;

public class ChallengeLevel2 implements GameMode {

    private static final int GOAL_LINES = 15;
    private static final int FREEZE_THRESHOLD = 10;

    // Config: Garbage rises every 15 ticks
    private static final int GARBAGE_INTERVAL = 15;


    //Config: 10 seconds freeze
    private static final int COUNTDOWN_DURATION = 25;

    // 3 seconds for the time freeze text display
    private static final int TEXT_DISPLAY_TICKS = 8;

    private int tickCounter = 0;
    private int countdownTimer = 0;
    private int notificationTimer = 0;
    private boolean bonusTriggered = false;

    @Override
    public String getName(){
        return "LEVEL 2";
    }

    @Override
    public String getDescription() {
        return "Garbage rises from below! Clear 15 lines to escape!\n" +
                "At 10 lines cleared, time FREEZES for a moment — use it wisely!";
    }

    @Override
    public void onLinesUpdated(int totalLines, GameLoopManager loopManager){

        loopManager.setRate(1.1);

        // trigger time freeze  at 20 lines
        if (totalLines >= FREEZE_THRESHOLD && !bonusTriggered){
            bonusTriggered = true;
            notificationTimer = TEXT_DISPLAY_TICKS;
            countdownTimer = COUNTDOWN_DURATION;
        }
    }

    @Override
    public boolean onGameTick(Board board){
        // if freeze is active, count down and stop gravity
        if (notificationTimer > 0){
            notificationTimer--;
            return false; //stop brick from falling
        }

        if (countdownTimer > 0) {
            countdownTimer--;
            return false; // gravity STOPPED
        }

        //manage garbage  wall (only if not frozen)
        tickCounter++;
        if(tickCounter >= GARBAGE_INTERVAL){
            tickCounter = 0;
            board.addGarbageLine();
        }

        return true; // apply gravity normally
    }

    @Override
    public String getOverlayMessage() {
        if (notificationTimer > 0) {
            return "SHOW_TEXT";
        }

        if (countdownTimer > 0) {
            int seconds = (int) Math.ceil(countdownTimer * 0.4);
            return String.valueOf(seconds);
        }
        return null;
    }

    @Override
    public boolean isWinConditionMet(int totalLines) {
        return totalLines >= GOAL_LINES;
    }

    @Override
    public GameMode getNextLevel() {
        return new ChallengeLevel3();
    }
}
