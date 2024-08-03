package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import ec.edu.controllers.ClientController;

public class RegisterPanel extends JPanel {
    private ClientController client;
    private Image backgroundImage;

    public RegisterPanel(ClientController client) {
        this.client = client;
        this.backgroundImage = new ImageIcon("implementacion/src/main/java/ec/edu/utils/any-colorful-uno-game-cards-on-blue-background-uno-is-an-american-shedding-type-card-ga (1).jpeg").getImage();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        usernameField.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(255, 255, 255));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(255, 255, 255));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 51, 102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }
}