package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import ec.edu.controllers.ClientController;

public class LoginPanel extends JPanel {
    private ClientController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Image backgroundImage;

    public LoginPanel(ClientController controller) {
        this.controller = controller;
        this.backgroundImage = new ImageIcon("implementacion/src/main/java/ec/edu/utils/any-colorful-uno-game-cards-on-blue-background-uno-is-an-american-shedding-type-card-ga (1).jpeg").getImage();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Welcome to Card Game");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setBorder(new LineBorder(Color.WHITE));
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setBorder(new LineBorder(Color.WHITE));
        gbc.gridx = 1;
        add(passwordField, gbc);

        loginButton = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        registerButton = new JButton("Register");
        gbc.gridy = 4;
        add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            controller.authenticateUser(username, password);
        });

        registerButton.addActionListener(e -> {
            // Switch to the RegisterPanel
            controller.switchToRegisterPanel();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
