package com.comp2042.util;

import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //private constructor to prevent instantiation
    private MatrixOperations(){}

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;

                //if the brick has a block at this local position
                if (brick[j][i] != 0) {
                    if (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        return targetY<0||targetX>=matrix.length||
                targetX<0||targetX>=matrix[0].length;
    }

    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[j][i] != 0) {
                    int targetX = x + i;
                    int targetY = y + j;

                    if (!checkOutOfBound(copy, targetX, targetY)) {
                        copy[targetY][targetX] = brick[j][i];
                    }
                }
            }
        }
        return copy;
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
