package com.comp2042.util;

import com.comp2042.model.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RowClearer {

    public RowClearingOutput checkRemoving(final int[][] matrix) {
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            if(isRowFull(matrix[i])) {
                clearedRows.add(i);
            } else {
                newRows.addLast(matrix[i].clone());
            }
        }
        return new RowClearingOutput(clearedRows.size(), buildNewMatrix(newRows, matrix.length, matrix[0].length), clearedRows);
    }

    private boolean isRowFull(int[] row) {
        for (int cell : row) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    private int[][] buildNewMatrix(Deque<int[]> newRows, int rows, int cols) {
        int[][] tmp = new int[rows][cols];

        for(int i=rows-1; i>=0; i--) {
            int[] row = newRows.pollLast();
            if(row != null) {
                tmp[i] = row;
            } else {
                break; // empty row
            }
        }
        return tmp;
    }

}
