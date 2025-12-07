package com.comp2042.game.events;

/**
 * Enumerates the possible sources of a game event.
 * <p>
 * This distinction is used to differentiate between actions initiated by the
 * player and actions initiated by the game loop
 * </p>
 */
public enum EventSource {
    USER, THREAD
}
