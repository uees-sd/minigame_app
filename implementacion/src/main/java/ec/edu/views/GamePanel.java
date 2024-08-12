package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import ec.edu.controllers.ClientController;

public class GamePanel extends JPanel {
    private ClientController client;
    private JButton abandonGameButton;  // Abandon Game Button
    private JButton passButton;
    private JButton skipButton; // Skip Button
    private JLabel messageLabel;
    private JLabel sumLabel;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private Set<Integer> blockedCards;
    private JProgressBar progressBar; // Progress Bar
    private Timer actionTimer; // Timer for Pass and Skip buttons
    private int remainingTime = 3; // Track remaining time

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
            cardButtons[i-1] = new JButton("" + i);
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
        bottomPanel.setLayout(new GridLayout(2, 3)); // Adjust layout to accommodate the new button

        passButton = new JButton("Pass");
        styleButton(passButton);
        passButton.addActionListener(e -> handlePassAction());
        bottomPanel.add(passButton);

        skipButton = new JButton("Skip");
        styleButton(skipButton);
        skipButton.addActionListener(e -> handleSkipAction());
        bottomPanel.add(skipButton);

        progressBar = new JProgressBar(0, 100); // Initialize progress bar
        progressBar.setValue(0); // Initial value
        progressBar.setStringPainted(true); // Show percentage
        bottomPanel.add(progressBar);

        messageLabel = new JLabel("Waiting for answers...");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(0, 51, 102));
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(messageLabel);

        abandonGameButton = new JButton("Abandon Game"); // New Abandon Game Button
        styleButton(abandonGameButton);
        abandonGameButton.addActionListener(e -> handleAbandonGameAction());
        bottomPanel.add(abandonGameButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initialize user list components
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        userList.setForeground(new Color(0, 51, 102));
        userList.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        JScrollPane userScrollPane = new JScrollPane(userList);
        add(userScrollPane, BorderLayout.EAST);

        // Initialize the timer for Pass and Skip buttons
        actionTimer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingTime <= 0) {
                    actionTimer.stop();
                    enableActionButtons();
                    messageLabel.setText("Es tu turno.");
                } else {
                    messageLabel.setText("Tiempo restante: " + remainingTime + " segundos");
                    remainingTime--;
                }
            }
        });
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

    private void handlePassAction() {
        if (client.getCurrentRoomCode() != null) {
            client.sendMessage("PASS:" + client.getCurrentRoomCode() + ":" + client.getUsername());
            restartActionTimer();
        }
    }

    private void handleSkipAction() {
        if (client.getCurrentRoomCode() != null) {
            client.sendMessage("SKIP:" + client.getCurrentRoomCode() + ":" + client.getUsername());
            restartActionTimer();
        }
    }

    private void handleAbandonGameAction() {
        if (client.getCurrentRoomCode() != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to abandon the game?", "Confirm Abandon", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                client.sendMessage("LEAVE_ROOM:" + client.getCurrentRoomCode() + ":" + client.getUsername());
                client.switchToRoomPanel(); // Redirect to room selection panel
            }
        }
    }

    private void restartActionTimer() {
        if (actionTimer.isRunning()) {
            actionTimer.stop();
        }
        remainingTime = 5; // Reset remaining time to 15 seconds
        actionTimer.start(); // Start the timer
        passButton.setEnabled(false);
        skipButton.setEnabled(false);
    }

    private void enableActionButtons() {
        passButton.setEnabled(true);
        skipButton.setEnabled(true);
        messageLabel.setText("Waiting for answers...");
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
        sumLabel.setText("Sum: " + a + " + " + b + " = ?" );
    }

    public void updateProgress() {
        int progress = blockedCards.size() * 10;
        progressBar.setValue(progress);
        // Ensure the progress is between 0 and 100
        //if es mayor a 100 o igual 100, alerta que gana y deja la sala
        if (progress >= 100) {
            JOptionPane.showMessageDialog(client.frame, "¡Felicidades! Has ganado el juego.");
            client.sendMessage("LEAVE_ROOM:" + client.getCurrentRoomCode() + ":" + client.getUsername());
            //redirige para que cree otra sala
            client.switchToRoomPanel();
        }
        progressBar.setValue(progress);
    }

    public void blockCard(int cardNumber) {
        blockedCards.add(cardNumber);
        // Change the color of the blocked card to gray
        for (Component comp : ((JPanel) getComponent(1)).getComponents()) {
            JButton button = (JButton) comp;
            if (button.getText().equals("" + cardNumber)) {
                button.setBackground(Color.GRAY);
            }
        }
        updateProgress();
    }    
}

