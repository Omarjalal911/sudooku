package sudoku;

import javax.swing.*;
import java.awt.*;

public class SignInFrame extends JFrame {
    public SignInFrame() {
        setTitle("Sign In");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Sign In");
        JButton switchToSignUp = new JButton("No account? Sign Up");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());

            User user = UserManager.login(username, pass);
            if (user != null) {
                new MainMenu(user);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.");
            }
        });

        switchToSignUp.addActionListener(e -> {
            new SignUpFrame();
            dispose();
        });

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        add(switchToSignUp, BorderLayout.NORTH);
        setVisible(true);
    }
}
