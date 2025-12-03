package com.comp2042.ui.input;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.ui.view.GameView;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.StackPane;

import java.util.function.BiConsumer;

public class InputController {

    public void bindInputs(StackPane rootPane,
                           GameView gameView,
                           EventDispatcher dispatcher,
                           BooleanProperty isPause,
                           BooleanProperty isGameOver,
                           BiConsumer<EventType, EventSource> onMoveDown,
                           Runnable onNewGame,
                           Runnable onTogglePause) {
        rootPane.setOnKeyPressed(new InputHandler(
                gameView,
                dispatcher,
                isPause,
                isGameOver,
                onMoveDown,
                onNewGame,
                onTogglePause
        ));
    }
}
