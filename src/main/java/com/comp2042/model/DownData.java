package com.comp2042.model;

/**
 * A container class used to pass state updates after a move down or drop event.
 * <p>
 * This class holds two types of results:
 * 1. {@link ViewData}: If the brick simply moved down successfully.
 * 2. {@link ClearRow}: If the brick landed and caused rows to be cleared.
 * </p>
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
