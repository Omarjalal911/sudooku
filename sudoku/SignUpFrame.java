package sudoku;

import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {
    public SignUpFrame() {
        setTitle("Sign Up");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JButton signUpButton = new JButton("Register");
        JButton switchToLogin = new JButton("Have an account? Sign In");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields.");
            } else if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
            } else if (UserManager.registerUser(username, pass)) {
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                new SignInFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        });

        switchToLogin.addActionListener(e -> {
            new SignInFrame();
            dispose();
        });

        add(panel, BorderLayout.CENTER);
        add(signUpButton, BorderLayout.SOUTH);
        add(switchToLogin, BorderLayout.NORTH);
        setVisible(true);
    }
}
