package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import ec.edu.controllers.ClientController;

public class GamePanel extends JPanel {
    private ClientController client;
    private JButton abandonGameButton;
    private JLabel messageLabel;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;

    public GamePanel(ClientController client) {
        this.client = client;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Initialize components
        JLabel referenceLabel = new JLabel("Select a card:");
        referenceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        referenceLabel.setForeground(new Color(0, 51, 102));
        referenceLabel.setHorizontalAlignment(JLabel.CENTER);
        referenceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(referenceLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(2, 5, 10, 10));
        cardPanel.setBackground(new Color(240, 248, 255));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton[] cardButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            cardButtons[i] = new JButton("Card " + (i + 1));
            styleButton(cardButtons[i]);
            int cardNumber = i + 1;
            cardButtons[i].addActionListener(e -> {
                if (client.getCurrentRoomCode() != null) {
                    client.sendMessage("SELECT_CARD:" + client.getCurrentRoomCode() + ":" + client.getUsername() + ":" + cardNumber);
                }
            });
            cardPanel.add(cardButtons[i]);
        }
        add(cardPanel, BorderLayout.CENTER);

        abandonGameButton = new JButton("Abandonar Partida");
        styleButton(abandonGameButton);
        abandonGameButton.addActionListener(e -> {
            if (client.getCurrentRoomCode() != null) {
                client.sendMessage("LEAVE_ROOM:" + client.getCurrentRoomCode() + ":" + client.getUsername());
                client.setCurrentRoomCode(null);
                client.switchToRoomPanel(); // Switch back to RoomPanel
            }
        });
        add(abandonGameButton, BorderLayout.SOUTH);

        messageLabel = new JLabel("Waiting for answers...");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(0, 51, 102));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(messageLabel, BorderLayout.SOUTH);

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
}
