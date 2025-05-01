// 📄 PlacementTest.java (مُحدث مع حدود 3x3 باستخدام MatteBorder)
package sudoku;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Stack;

public class PlacementTest extends JFrame {
    private SudokuGameManager gameManager;
    private JTextField[][] fields;
    private JLabel timerLabel;
    private JLabel mistakesLabel;
    private int mistakes = 0;
    private int secondsPassed = 0;
    private Timer timer;
    private User user;
    private JTextField selectedField;
    private boolean pencilMode = false;
    private Stack<Move> undoStack = new Stack<>();

    public PlacementTest(User user) {
        this.user = user;
        this.fields = new JTextField[9][9];
        this.gameManager = new SudokuGameManager(40);

        setTitle("Placement Test");
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        boardPanel.setPreferredSize(new Dimension(450, 450));

        Font font = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField field = new JTextField();
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(font);

                // حدود عريضة بين مربعات 3×3
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;
                field.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                SudokuCell cell = gameManager.getBoard().getCell(row, col);
                if (cell.isFixed()) {
                    field.setText(String.valueOf(cell.getValue()));
                    field.setEditable(false);
                    field.setBackground(new Color(210, 230, 255));
                } else {
                    field.setEditable(false);
                    field.setBackground(Color.WHITE);
                    field.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            selectedField = field;
                        }
                    });
                }

                fields[row][col] = field;
                boardPanel.add(field);
            }
        }

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        timerLabel = new JLabel("Time: 0s");
        mistakesLabel = new JLabel("Mistakes: 0 / 3");
        infoPanel.add(timerLabel);
        infoPanel.add(mistakesLabel);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        for (int i = 1; i <= 9; i++) {
            int number = i;
            JButton btn = new JButton(String.valueOf(i));
            btn.addActionListener(e -> fillNumber(number));
            buttonsPanel.add(btn);
        }

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoMove());
        JButton eraseButton = new JButton("Erase");
        eraseButton.addActionListener(e -> eraseField());
        JButton pencilButton = new JButton("Pencil");
        pencilButton.addActionListener(e -> {
            pencilMode = !pencilMode;
            pencilButton.setText(pencilMode ? "Pencil: ON" : "Pencil");
        });

        buttonsPanel.add(undoButton);
        buttonsPanel.add(eraseButton);
        buttonsPanel.add(pencilButton);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        startTimer();
        setVisible(true);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            secondsPassed++;
            timerLabel.setText("Time: " + secondsPassed + "s");
        });
        timer.start();
    }

    private void fillNumber(int number) {
        if (selectedField == null) return;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (fields[row][col] == selectedField) {
                    if (pencilMode) {
                        selectedField.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        selectedField.setForeground(Color.GRAY);
                        selectedField.setText(String.valueOf(number));
                        return;
                    }
                    int correct = gameManager.getSolutionAt(row, col);
                    undoStack.push(new Move(row, col, fields[row][col].getText()));
                    if (number == correct) {
                        fields[row][col].setText(String.valueOf(number));
                        fields[row][col].setBackground(Color.WHITE);
                        fields[row][col].setForeground(Color.BLACK);
                        selectedField.setFont(new Font("SansSerif", Font.BOLD, 20));
                        gameManager.getBoard().setCellValue(row, col, number);
                        if (gameManager.isGameComplete()) {
                            timer.stop();
                            evaluateLevel();
                        }
                    } else {
                        fields[row][col].setText(String.valueOf(number));
                        fields[row][col].setBackground(new Color(255, 180, 180));
                        mistakes++;
                        mistakesLabel.setText("Mistakes: " + mistakes + " / 3");
                        if (mistakes >= 3) {
                            timer.stop();
                            JOptionPane.showMessageDialog(this, "You failed the placement test.");
                            new MainMenu(user);
                            dispose();
                        }
                    }
                    return;
                }
            }
        }
    }

    private void eraseField() {
        if (selectedField != null && selectedField.isEditable()) {
            selectedField.setText("");
            selectedField.setFont(new Font("SansSerif", Font.BOLD, 20));
            selectedField.setForeground(Color.BLACK);
        }
    }

    private void undoMove() {
        if (!undoStack.isEmpty()) {
            Move last = undoStack.pop();
            fields[last.row][last.col].setText(last.previousValue);
            fields[last.row][last.col].setBackground(Color.WHITE);
        }
    }

    private void evaluateLevel() {
        String level;
        if (mistakes < 5 && secondsPassed < 600) level = "Advanced";
        else if (mistakes <= 10) level = "Normal";
        else level = "Beginner";

        user.addXP(10);
        UserManager.saveUsers();

        JOptionPane.showMessageDialog(this,
                "Test Complete!\nLevel: " + level + "\nXP +10");

        new MainMenu(user);
        dispose();
    }

    private static class Move {
        int row, col;
        String previousValue;

        public Move(int row, int col, String value) {
            this.row = row;
            this.col = col;
            this.previousValue = value;
        }
    }
}
