package sudoku;

public class SudokuGameManager {
    private SudokuBoard board;
    private int[][] solution;

    public SudokuGameManager(int removeCount) {
        int[][] fullBoard = SudokuGenerator.generateBoard(0);
        solution = deepCopy(fullBoard);
        SudokuGenerator.removeNumbers(fullBoard, removeCount);
        board = new SudokuBoard(fullBoard);
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public int getSolutionAt(int row, int col) {
        return solution[row][col];
    }

    public boolean isMoveValid(int row, int col, int value) {
        return board.isValidMove(row, col, value);
    }

    public boolean isGameComplete() {
        return board.isComplete();
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }
}
