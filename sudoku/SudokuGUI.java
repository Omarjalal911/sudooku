package sudoku;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Stack;

public class SudokuGUI extends JFrame {
    private User user;
    private String level;
    private SudokuGameManager gameManager;
    private JTextField[][] fields;
    private JTextField selectedField;
    private JLabel xpLabel, timerLabel, mistakesLabel;
    private boolean pencilMode = false;
    private Stack<Move> undoStack = new Stack<>();
    private int mistakes = 0;
    private int secondsPassed = 0;
    private Timer timer;

    public SudokuGUI(User user, String level) {
        this.user = user;
        this.level = level;
        this.gameManager = new SudokuGameManager(getRemoveCountByLevel(level));
        this.fields = new JTextField[9][9];

        setTitle("Sudoku - " + level + " | " + user.getUsername());
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Board Panel ===
        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        boardPanel.setPreferredSize(new Dimension(450, 450));
        Font font = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField field = new JTextField();
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(font);

                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;
                field.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                SudokuCell cell = gameManager.getBoard().getCell(row, col);
                if (cell.isFixed()) {
                    field.setText(String.valueOf(cell.getValue()));
                    field.setEditable(false);
                    field.setBackground(new Color(210, 230, 255));
                } else {
                    field.setEditable(false);
                    field.setBackground(Color.WHITE);
                    field.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            selectedField = field;
                        }
                    });
                }

                fields[row][col] = field;
                boardPanel.add(field);
            }
        }

        // === Info Panel ===
        JPanel infoPanel = new JPanel(new GridLayout(1, 4));
        xpLabel = new JLabel("XP: " + user.getXP());
        timerLabel = new JLabel("Time: 0s");
        mistakesLabel = new JLabel("Mistakes: 0");
        JButton hintButton = new JButton("Hint (-5 XP)");

        hintButton.addActionListener(e -> {
            if (user.useXP(5)) {
                giveHint();
                xpLabel.setText("XP: " + user.getXP());
            } else {
                JOptionPane.showMessageDialog(this, "Not enough XP!");
            }
        });

        infoPanel.add(xpLabel);
        infoPanel.add(hintButton);
        infoPanel.add(timerLabel);
        infoPanel.add(mistakesLabel);

        // === Buttons Panel ===
        JPanel buttonPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        for (int i = 1; i <= 9; i++) {
            int number = i;
            JButton btn = new JButton(String.valueOf(i));
            btn.addActionListener(e -> fillNumber(number));
            buttonPanel.add(btn);
        }

        JButton pencilButton = new JButton("Pencil");
        pencilButton.addActionListener(e -> {
            pencilMode = !pencilMode;
            pencilButton.setText(pencilMode ? "Pencil: ON" : "Pencil");
        });

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undoMove());

        JButton eraseButton = new JButton("Erase");
        eraseButton.addActionListener(e -> eraseField());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            UserManager.saveUsers();
            new MainMenu(user);
            dispose();
        });

        buttonPanel.add(pencilButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(eraseButton);
        buttonPanel.add(backButton);

        // Add all panels to frame
        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

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
                        selectedField.setText(String.valueOf(number));
                        selectedField.setForeground(Color.GRAY);
                        selectedField.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        return;
                    }

                    int correct = gameManager.getSolutionAt(row, col);
                    undoStack.push(new Move(row, col, fields[row][col].getText()));

                    if (number == correct) {
                        fields[row][col].setText(String.valueOf(number));
                        fields[row][col].setBackground(Color.WHITE);
                        fields[row][col].setForeground(Color.BLACK);
                        fields[row][col].setFont(new Font("SansSerif", Font.BOLD, 20));
                        gameManager.getBoard().setCellValue(row, col, number);

                        if (gameManager.isGameComplete()) {
                            timer.stop();
                            user.addXP(10);
                            UserManager.saveUsers();
                            JOptionPane.showMessageDialog(this, "You win! XP +10");
                            new MainMenu(user);
                            dispose();
                        }
                    } else {
                        fields[row][col].setText(String.valueOf(number));
                        fields[row][col].setBackground(new Color(255, 180, 180));
                        mistakes++;
                        mistakesLabel.setText("Mistakes: " + mistakes);
                    }
                    return;
                }
            }
        }
    }

    private void eraseField() {
        if (selectedField != null) {
            selectedField.setText("");
            selectedField.setFont(new Font("SansSerif", Font.BOLD, 20));
            selectedField.setForeground(Color.BLACK);
        }
    }

    private void undoMove() {
        if (!undoStack.isEmpty()) {
            Move move = undoStack.pop();
            fields[move.row][move.col].setText(move.previousValue);
        }
    }

    private void giveHint() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gameManager.getBoard().getCell(row, col).getValue() == 0) {
                    int correct = gameManager.getSolutionAt(row, col);
                    fields[row][col].setText(String.valueOf(correct));
                    fields[row][col].setBackground(new Color(200, 255, 200));
                    gameManager.getBoard().setCellValue(row, col, correct);
                    return;
                }
            }
        }
    }

    private int getRemoveCountByLevel(String level) {
        return switch (level) {
            case "Easy" -> 30;
            case "Normal" -> 40;
            case "Hard" -> 50;
            default -> 40;
        };
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
