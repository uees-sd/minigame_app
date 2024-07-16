package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientView extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel loginPanel;

    public ClientView() {
        setTitle("Chat Client");
        setSize(600, 400); // Tamaño ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para el chat
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        inputField = new JTextField(30);
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Panel para login y registro
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1)); // Ajuste de diseño para botones

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginPanel.add(new JLabel("Welcome to the App!", SwingConstants.CENTER));
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(chatPanel, BorderLayout.CENTER);
        mainPanel.add(loginPanel, BorderLayout.WEST); // Panel de login a la izquierda

        add(mainPanel);
        setVisible(true);
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInputText() {
        inputField.setText("");
    }

    public void addSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void showLoginPanel(boolean show) {
        loginPanel.setVisible(show);
    }

    public void enableChat(boolean enable) {
        inputField.setEnabled(enable);
        sendButton.setEnabled(enable);
        chatArea.setEnabled(enable);
    }
}
