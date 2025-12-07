package com.comp2042.ui.view;

import com.comp2042.game.core.GameLoopManager;
import javafx.beans.property.BooleanProperty;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Coordinates the execution of game animations while ensuring game state consistency.
 * <p>
 * This class handles pausing the game loop during animations
 * and restoring the game state once the visual effects are finished.
 * </p>
 */
public class AnimationCoordinator {

    private final GameLoopManager gameLoopManager;
    private final BooleanProperty isPause;
    private final Consumer<Boolean> uiVisibilitySetter;
    private boolean isAnimating = false;

    public AnimationCoordinator(GameLoopManager gameLoopManager, BooleanProperty isPause, Consumer<Boolean> uiVisibilitySetter) {
        this.gameLoopManager = gameLoopManager;
        this.isPause = isPause;
        this.uiVisibilitySetter = uiVisibilitySetter;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    /**
     * Creates a callback that restores the UI and resumes the game loop only if the completion check passes.
     */
    public Runnable createFinishes(Runnable logicCallback, Supplier<Boolean> completionCheck){
        return ()->{
            uiVisibilitySetter.accept(true);
            logicCallback.run();

            if (!completionCheck.get()) {
                isPause.setValue(false);
                isAnimating = false;
                gameLoopManager.play();
            }
        };
    }

    /**
     * Pauses the game, hides the static bricks, and runs the provided animation starter.
     */
    public void runAnimationFlow(Runnable animationStarter){
        isAnimating = true;
        isPause.setValue(true);
        gameLoopManager.pause();
        uiVisibilitySetter.accept(false);

        animationStarter.run();
    }
}
