package sudoku;

import java.util.Random;

public class SudokuGenerator {

    public static int[][] generateBoard(int removeCount) {
        int[][] board = new int[9][9];

        fillDiagonalBoxes(board);
        solve(board);
        removeNumbers(board, removeCount);

        return board;
    }

    private static void fillDiagonalBoxes(int[][] board) {
        Random rand = new Random();
        for (int k = 0; k < 9; k += 3) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int num;
                    do {
                        num = rand.nextInt(9) + 1;
                    } while (!isSafeInBox(board, k, k, num));
                    board[k + i][k + j] = num;
                }
            }
        }
    }

    private static boolean isSafeInBox(int[][] board, int startRow, int startCol, int num) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[startRow + i][startCol + j] == num)
                    return false;
        return true;
    }

    private static boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSafe(int[][] board, int row, int col, int num) {
        for (int d = 0; d < 9; d++) {
            if (board[row][d] == num || board[d][col] == num)
                return false;
        }
        return isSafeInBox(board, row - row % 3, col - col % 3, num);
    }

    public static void removeNumbers(int[][] board, int count) {
        Random rand = new Random();
        while (count > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                count--;
            }
        }
    }
}
