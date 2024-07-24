package ec.edu.views;

import javax.swing.*;

import ec.edu.controllers.ClientController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
    private ClientController client;
    private JButton[] cardButtons;
    private JLabel messageLabel;

    public GamePanel(ClientController client) {
        this.client = client;
        setLayout(new BorderLayout());

        JLabel referenceLabel = new JLabel("Select a card:");
        add(referenceLabel, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(2, 5));
        cardButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            cardButtons[i] = new JButton("Card " + (i + 1));
            int cardNumber = i + 1;
            cardButtons[i].addActionListener(e -> {
                if (client.getCurrentRoomCode() != null) {
                    client.sendMessage("SELECT_CARD:" + client.getCurrentRoomCode() + ":" + client.getUsername() + ":" + cardNumber);
                }
            });
            cardPanel.add(cardButtons[i]);
        }
        add(cardPanel, BorderLayout.CENTER);

        messageLabel = new JLabel(" ");
        add(messageLabel, BorderLayout.SOUTH);
    }

    public void updateMessage(String message) {
        messageLabel.setText(message);
    }
}
