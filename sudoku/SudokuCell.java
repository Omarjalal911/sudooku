package sudoku;

import java.io.Serializable;

public class SudokuCell implements Serializable {
    private int value;
    private boolean fixed;

    public SudokuCell(int value, boolean fixed) {
        this.value = value;
        this.fixed = fixed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (!fixed) {
            this.value = value;
        }
    }

    public boolean isFixed() {
        return fixed;
    }
}
