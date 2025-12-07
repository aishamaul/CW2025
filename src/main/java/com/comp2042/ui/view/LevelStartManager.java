package com.comp2042.ui.view;

import com.comp2042.ui.view.menu.LevelMenuController;

/**
 * Manages the transition screens shown at the start of a level.
 * <p>
 * This class handles setting up the callbacks for the level menu buttons
 * and triggering the display of the start screen overlay.
 * </p>
 */
public class LevelStartManager {

    /**
     * Registers the action callbacks for the level menu controller.
     * @param levelMenusController The controller for the menu UI.
     * @param onStartLevel         Runnable to execute when "Start" is clicked.
     * @param onNextLevel          Runnable to execute when "Next Level" is clicked.
     * @param onExit               Runnable to execute when "Exit" is clicked.
     */
    public void registerCallbacks(LevelMenuController levelMenusController,
                                  Runnable onStartLevel,
                                  Runnable onNextLevel,
                                  Runnable onExit) {
        if (levelMenusController != null) {
            levelMenusController.setCallbacks(
                    onStartLevel,
                    onNextLevel,
                    onExit
            );
            levelMenusController.hideAll();
        }
    }

    /**
     * Displays the start screen for a specific level.
     *
     * @param controller  The menu controller.
     * @param modeName    The name of the level (e.g., "LEVEL 1").
     * @param description The description of the level objectives.
     */
    public void showStartScreen(LevelMenuController controller, String modeName, String description) {
        if (controller != null) {
            controller.showStartScreen(modeName, description);
        }
    }
}
