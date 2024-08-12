package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import ec.edu.controllers.ClientController;

public class GamePanel extends JPanel {
    private ClientController client;
    private JButton abandonGameButton;
    private JButton passButton;
    private JButton skipButton; // New Skip Button
    private JLabel messageLabel;
    private JLabel sumLabel;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private Set<Integer> blockedCards;

    public GamePanel(ClientController client) {
        this.client = client;
        this.blockedCards = new HashSet<>();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Initialize components
        sumLabel = new JLabel("Sum: 0 + 0 = 0");
        sumLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sumLabel.setForeground(new Color(0, 51, 102));
        sumLabel.setHorizontalAlignment(JLabel.CENTER);
        sumLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(sumLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(2, 5, 10, 10));
        cardPanel.setBackground(new Color(240, 248, 255));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton[] cardButtons = new JButton[10];
        for (int i = 1; i <= 10; i++) {
            cardButtons[i-1] = new JButton("Card " + i);
            styleButton(cardButtons[i-1]);
            int cardNumber = i;
            cardButtons[i-1].addActionListener(e -> {
                if (client.getCurrentRoomCode() != null && !blockedCards.contains(cardNumber)) {
                    client.sendMessage("SELECT_CARD:" + client.getCurrentRoomCode() + ":" + client.getUsername() + ":" + cardNumber);
                }
            });
            cardPanel.add(cardButtons[i-1]);
        }
        add(cardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3)); // Adjust layout to fit three buttons

        passButton = new JButton("Pass");
        styleButton(passButton);
        passButton.addActionListener(e -> {
            if (client.getCurrentRoomCode() != null) {
                client.sendMessage("PASS:" + client.getCurrentRoomCode() + ":" + client.getUsername());
            }
        });
        bottomPanel.add(passButton);

        skipButton = new JButton("Skip"); // New Skip Button
        styleButton(skipButton);
        skipButton.addActionListener(e -> {
            if (client.getCurrentRoomCode() != null) {
                client.sendMessage("SKIP:" + client.getCurrentRoomCode() + ":" + client.getUsername());
            }
        });
        bottomPanel.add(skipButton);

        messageLabel = new JLabel("Waiting for answers...");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(0, 51, 102));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(messageLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize user list components
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        userList.setForeground(new Color(0, 51, 102));
        userList.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        JScrollPane userScrollPane = new JScrollPane(userList);
        add(userScrollPane, BorderLayout.EAST);
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

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }

    public void updateUserList(String[] users) {
        userListModel.clear();
        for (String user : users) {
            userListModel.addElement(user);
        }
    }

    public void updateSum(int a, int b) {
        sumLabel.setText("Sum: " + a + " + " + b + " = " + (a + b));
    }

    public void blockCard(int cardNumber) {
        blockedCards.add(cardNumber);
        // Change the color of the blocked card to gray (implementation detail depends on how you manage the buttons)
        for (Component comp : ((JPanel) getComponent(1)).getComponents()) {
            JButton button = (JButton) comp;
            if (button.getText().equals("Card " + cardNumber)) {
                button.setBackground(Color.GRAY);
            }
        }
    }
}
