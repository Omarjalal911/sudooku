package sudoku;

import java.io.Serializable;

public class SudokuBoard implements Serializable {
    private SudokuCell[][] board;
    public static final int SIZE = 9;

    public SudokuBoard(int[][] values) {
        board = new SudokuCell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boolean fixed = values[i][j] != 0;
                board[i][j] = new SudokuCell(values[i][j], fixed);
            }
        }
    }

    public SudokuCell getCell(int row, int col) {
        return board[row][col];
    }

    public void setCellValue(int row, int col, int value) {
        board[row][col].setValue(value);
    }

    public boolean isComplete() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].getValue() == 0) return false;
            }
        }
        return true;
    }

    public boolean isValidMove(int row, int col, int value) {
        for (int j = 0; j < SIZE; j++)
            if (board[row][j].getValue() == value) return false;
        for (int i = 0; i < SIZE; i++)
            if (board[i][col].getValue() == value) return false;
        int boxRow = row / 3 * 3;
        int boxCol = col / 3 * 3;
        for (int i = boxRow; i < boxRow + 3; i++)
            for (int j = boxCol; j < boxCol + 3; j++)
                if (board[i][j].getValue() == value) return false;
        return true;
    }
}
