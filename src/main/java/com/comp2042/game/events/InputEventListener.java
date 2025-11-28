package com.comp2042.game.events;

import com.comp2042.game.mode.GameMode;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    DownData onDropEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    void createNewGame();

    boolean onGameTick(GameMode mode);
}
