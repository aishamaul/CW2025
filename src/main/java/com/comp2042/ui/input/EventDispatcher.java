package com.comp2042.ui.input;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.game.events.InputEventListener;
import com.comp2042.game.events.MoveEvent;
import com.comp2042.game.mode.GameMode;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

public class EventDispatcher {

    private final InputEventListener eventListener;

    public EventDispatcher(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setGameMode(GameMode mode) {
        eventListener.setGameMode(mode);
    }

    public ViewData moveLeft() {
        return eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
    }

    public ViewData moveRight() {
        return eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
    }

    public ViewData rotate() {
        return eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
    }

    public void newGame() {
        eventListener.createNewGame();
    }

    public DownData moveDown(EventType eventType, EventSource eventSource) {
        MoveEvent event = new MoveEvent(eventType, eventSource);
        if(eventType == EventType.DROP) {
            return eventListener.onDropEvent(event);
        }else{
            return eventListener.onDownEvent(event);
        }
    }

    public ViewData hold(){
        return eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
    }

    public boolean onGameTick(GameMode mode) {
        return eventListener.onGameTick(mode);
    }
}
