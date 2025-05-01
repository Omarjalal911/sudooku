package sudoku;

import javax.swing.*;
import java.awt.*;

public class TutorialFrame extends JFrame {
    public TutorialFrame() {
        setTitle("How to Play Sudoku");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea tutorial = new JTextArea();
        tutorial.setEditable(false);
        tutorial.setLineWrap(true);
        tutorial.setWrapStyleWord(true);
        tutorial.setFont(new Font("SansSerif", Font.PLAIN, 16));

        tutorial.setText(
                "Welcome to Sudoku!\n\n" +
                "Goal:\n" +
                "- Fill the 9x9 grid so that each row, each column, and each 3x3 box contains the numbers 1 through 9.\n\n" +
                "Rules:\n" +
                "- No repeating numbers in rows, columns, or boxes.\n\n" +
                "Tips:\n" +
                "- Start with obvious cells.\n" +
                "- Use Pencil Mode to take notes.\n" +
                "- Use the Hint button (costs XP!)\n" +
                "- Try Placement Test if you're new!"
        );

        JScrollPane scrollPane = new JScrollPane(tutorial);
        add(scrollPane);

        setVisible(true);
    }
}
