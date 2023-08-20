package com.company;

// RUYA BOZCAN
// COMPUTER ENGINEERING
// ABU

import java.awt.Color;

public class ConnectFour {

    public static final int COLUMNS = 7;
    public static final int ROWS = 6;
    public static final Color COMPUTER = Color.YELLOW;
    public static final Color HUMAN = Color.RED;
    public static final Color NONE = Color.WHITE;

    public static void drop(Color[][] board, Color color, int column) {

        for (int r = 0; r < ROWS; r++) {
            if (board[r][column] == NONE) {
                board[r][column] = color;
                break;
            }
        }
    }

    public static boolean full(Color[][] board) {

        for (int c = 0; c < COLUMNS; c++) {
            if (board[ROWS - 1][c] == NONE) {
                return false;
            }
        }
        return true;
    }

    public static boolean legal(java.awt.Color[][] board, int column) {
        return column >= 0 && column < COLUMNS && board[ROWS - 1][column] == NONE;
    }

    public static Color opposite(java.awt.Color color) {
        return color == COMPUTER ? HUMAN : COMPUTER;
    }

    public static Color winAt(Color[][] board, int r, int c, int rowOffset, int colOffset) {

        Color possible = board[r][c];
        int row, col, multiplier = 1;
        if (possible == NONE) {
            return NONE;
        }
        do {
            if (multiplier == 4) {
                return possible;
            }
            row = rowOffset * multiplier + r;
            col = colOffset * multiplier + c;
            multiplier++;

        } while (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS && board[row][col] == possible);
        return NONE;
    }

    public static Color winner(Color[][] board) {

        Color color;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                for (int r_offset = -1; r_offset <= 1; r_offset++) {
                    for (int c_offset = -1; c_offset <= 1; c_offset++) {
                        if (!(r_offset == 0 && c_offset == 0)) {
                            color = winAt(board, row, col, r_offset, c_offset);
                            if (color != NONE) {
                                return color;
                            }
                        }
                    }
                }
            }
        }
        return NONE;
    }

    public static int bestMoveForComputer(Color[][] board, int maxDepth) {

        int bestResult = -2, bestCol = 0;
        for (int c = 0; c < COLUMNS; c++) {
            if (legal(board, c)) {
                drop(board, COMPUTER, c);
                int result = min(board, maxDepth, 0);
                undo(board, c);
                if (result >= bestResult) {
                    bestResult = result;
                    bestCol = c;
                }
            }
        }
        return bestCol;
    }

    public static int max(Color[][] board, int maxDepth, int depth) {

        Color winner = winner(board);
        if (winner == HUMAN) {
            return -1;
        } else if (winner == COMPUTER) {
            return 1;
        } else if (full(board) || (depth == maxDepth)) {
            return 0;
        } else {
            int bestResult = -2;

            for (int c = 0; c < COLUMNS; c++) {
                if (legal(board, c)) {
                    drop(board, COMPUTER, c);

                    int result = min(board, maxDepth, depth + 1);

                    undo(board, c);

                    if (result >= bestResult) {
                        bestResult = result;
                    }
                }
            }
            return bestResult;

        }
    }

    public static int min(Color[][] board, int maxDepth, int depth) {

        Color winner = winner(board);
        if (winner == COMPUTER) {
            return 1;
        } else if (winner == HUMAN) {
            return -1;
        } else if (full(board) || (depth == maxDepth)) {
            return 0;
        } else {

            int bestResult = 2;

            for (int c = 0; c < COLUMNS; c++) {
                if (legal(board, c)) {

                    drop(board, HUMAN, c);

                    int result = max(board, maxDepth, depth + 1);

                    undo(board, c);

                    if (result <= bestResult) {

                        bestResult = result;
                    }
                }
            }
            return bestResult;
        }
    }

    public static void undo(java.awt.Color[][] board, int column) {

        int row = ROWS - 1;
        while (board[row][column] == NONE && row > 0) {
            row--;
        }
        board[row][column] = NONE;
    }

    public static void main(String[] args) {

        Color[][] board = new Color[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = NONE;
            }
        }
        ConnectFourGUI.showGUI(board, HUMAN, 6);
    }

}

// RUYA BOZCAN
// COMPUTER ENGINEERING
// ABU
