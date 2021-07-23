package com.example.tictactoe;

import java.io.Serializable;

public class Map implements Serializable {
    public static final char DOT_X = 'X';
    public static final char DOT_0 = 'O';
    public static final char DOT_EMPTY = Character.MIN_VALUE;

    private int size;
    private char[][] arr;

    public Map(int size) {
        this.size = size;
        arr = new char[size][size];
    }

    public int getSize() {
        return size;
    }

    public boolean setDot(int i, int j, char dot){
        if (arr[i][j]==DOT_X || arr[i][j]==DOT_0){
            return false;
        } else {
            arr[i][j]=dot;
            return true;
        }
    }

    public char getDot(int i, int j){
        return arr[i][j];
    }

    public void clearMap(){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j]=Character.MIN_VALUE;
            }
        }
    }
}
