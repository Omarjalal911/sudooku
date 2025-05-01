package sudoku;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    public MainMenu(User user) {
        setTitle("Sudoku - Main Menu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel welcome = new JLabel("Welcome, " + user.getUsername() + " | XP: " + user.getXP(), SwingConstants.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton playButton = new JButton("Play");
        JButton placementButton = new JButton("Placement Test");
        JButton howToButton = new JButton("How to Play");
        JButton logoutButton = new JButton("Sign Out");

        playButton.addActionListener(e -> {
            String[] options = {"Easy", "Normal", "Hard"};
            int level = JOptionPane.showOptionDialog(this, "Choose difficulty:",
                    "Select Level", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (level != -1) {
                String levelStr = options[level];
                new SudokuGUI(user, levelStr);
                dispose();
            }
        });

        placementButton.addActionListener(e -> {
            new PlacementTest(user);
            dispose();
        });

        howToButton.addActionListener(e -> new TutorialFrame());

        logoutButton.addActionListener(e -> {
            UserManager.saveUsers();
            new SignInFrame();
            dispose();
        });

        add(welcome);
        add(playButton);
        add(placementButton);
        add(howToButton);
        add(logoutButton);

        setVisible(true);
    }
}
