package ec.edu.views;

import javax.swing.*;

import ec.edu.controllers.ClientController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    private ClientController client;

    public RegisterPanel(ClientController client) {
        this.client = client;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Register");
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

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(backButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(client.frame, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (client.registerUser(username, password)) {
                JOptionPane.showMessageDialog(client.frame, "Registration successful. Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                client.switchToLoginPanel();
            } else {
                JOptionPane.showMessageDialog(client.frame, "Username already taken", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> client.switchToLoginPanel());
    }
}
