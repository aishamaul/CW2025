package com.comp2042.game.events;

import com.comp2042.game.mode.GameMode;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

/**
 * Interface for listening to and processing input events from the UI.
 * <p>
 * Implementations of this interface (typically the GameController) act as the bridge
 * between the user interface and the game model, handling requests to move, rotate,
 * or modify the game state.
 * </p>
 */
public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    DownData onDropEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    void createNewGame();

    boolean onGameTick(GameMode mode);

    void setGameMode(GameMode mode);
}
