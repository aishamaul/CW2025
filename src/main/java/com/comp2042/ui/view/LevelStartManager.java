package com.comp2042.ui.view;

import com.comp2042.ui.view.menu.LevelMenuController;

public class LevelStartManager {

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
    public void showStartScreen(LevelMenuController controller, String modeName, String description) {
        if (controller != null) {
            controller.showStartScreen(modeName, description);
        }
    }
}
