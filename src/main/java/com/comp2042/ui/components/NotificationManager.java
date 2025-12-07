package com.comp2042.ui.components;

import javafx.scene.Group;

/**
 * Manages the lifecycle of notification popups in the game UI.
 * <p>
 * This class handles creating notification panels, adding them to the scene graph,
 * triggering their animations, and ensuring they are removed when done.
 * </p>
 */
public class NotificationManager {

    private final Group groupNotification;
    private final NotificationAnimator animator;

    public NotificationManager(Group groupNotification) {

        this.groupNotification = groupNotification;
        this.animator = new NotificationAnimator();
    }

    /**
     * Displays a floating score notification with the specified text.
     * @param text The text to display.
     */
    public void showScore(String text){
        NotificationPanel notificationPanel = new NotificationPanel(text);
        groupNotification.getChildren().add(notificationPanel);


        Runnable cleanup = () -> {groupNotification.getChildren().remove(notificationPanel);};
        animator.animate(notificationPanel, cleanup);
    }
}
