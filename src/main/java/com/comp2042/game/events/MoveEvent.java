package com.comp2042.game.events;

/**
 * Represent a specific game event, encapsulating the type of action and its origin.
 * <p>
 *     This class serves as a data carrier passed between the UI  input layer and the
 *     core game logic to request changes in the game state.
 * </p>
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     * @param eventType   The type of action.
     * @param eventSource The origin of the event.
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Retrieves the source of this event.
     *
     * @return The {@link EventSource} indicating who triggered the event.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
