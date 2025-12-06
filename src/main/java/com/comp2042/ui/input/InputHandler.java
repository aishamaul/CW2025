package com.comp2042.ui.input;

import com.comp2042.game.events.EventSource;
import com.comp2042.game.events.EventType;
import com.comp2042.ui.view.GameView;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.function.BiConsumer;

public class InputHandler implements EventHandler<KeyEvent> {
    private final GameView gameView;
    private final EventDispatcher dispatcher;
    private final BooleanProperty isPause;
    private final BooleanProperty isGameOver;
    private final BiConsumer<EventType, EventSource> onMoveDown;
    private final Runnable onNewGame;
    private final Runnable onTogglePause;

    public InputHandler(GameView gameView,
                        EventDispatcher dispatcher,
                        BooleanProperty isPause,
                        BooleanProperty isGameOver,
                        BiConsumer<EventType, EventSource> onMoveDown,
                        Runnable onNewGame,
                        Runnable onTogglePause) {
        this.gameView = gameView;
        this.dispatcher = dispatcher;
        this.isPause = isPause;
        this.isGameOver = isGameOver;
        this.onMoveDown = onMoveDown;
        this.onNewGame = onNewGame;
        this.onTogglePause = onTogglePause;
    }

    @Override
    public void handle(KeyEvent keyEvent) {

        //handle global game state (pause/new game)
        if (handleGameStateInput(keyEvent)){
            keyEvent.consume();
            return;
        }

        //stop if game is not running
        if (isPause.get()|| isGameOver.get()) {
            return;
        }

        handleMovementInput(keyEvent);
    }

    //extracted method: returns true if a state key was pressed
    private boolean handleGameStateInput(KeyEvent keyEvent) {
        return switch (keyEvent.getCode()) {
            case ESCAPE -> {
                if (!isPause.get()) {
                    onTogglePause.run();
                }
                yield true;
            }
            case N -> {
                onNewGame.run();
                yield true;
            }
            default -> false;
        };
    }

    //extracted method: handles movement logic
    private void handleMovementInput(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT, A -> {
                gameView.refreshBrick(dispatcher.moveLeft());
                keyEvent.consume();
            }
            case RIGHT, D -> {
                gameView.refreshBrick(dispatcher.moveRight());
                keyEvent.consume();
            }

            case UP, W -> {
                gameView.refreshBrick(dispatcher.rotate());
                keyEvent.consume();
            }

            case DOWN, S -> {
                onMoveDown.accept(EventType.DOWN, EventSource.USER);
                keyEvent.consume();
            }

            case SPACE -> {
                onMoveDown.accept(EventType.DROP, EventSource.USER);
                keyEvent.consume();
            }

            case H -> {
                gameView.refreshBrick(dispatcher.hold());
                keyEvent.consume();
            }

            default -> {
            }
        }
    }
}