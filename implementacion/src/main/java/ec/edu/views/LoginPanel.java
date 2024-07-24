package ec.edu.views;

import javax.swing.*;

import ec.edu.controllers.ClientController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private ClientController client;

    public LoginPanel(ClientController client) {
        this.client = client;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Welcome to the Card Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(client.frame, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (client.authenticateUser(username, password)) {
                client.setUsername(username);
                client.switchToRoomPanel();
            } else {
                JOptionPane.showMessageDialog(client.frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> client.switchToRegisterPanel());
    }
}
