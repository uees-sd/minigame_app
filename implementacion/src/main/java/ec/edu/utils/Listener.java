package ec.edu.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import javax.swing.JOptionPane;
import ec.edu.controllers.ClientController;

public class Listener implements Runnable {
    private ClientController clientController;

    public Listener(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                clientController.socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                handleMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(String message) {
        String[] parts = message.split(":");
        String command = parts[0];

        switch (command) {
            case "AUTH_SUCCESS":
                clientController.switchToRoomPanel();
                break;
            case "AUTH_FAIL":
                JOptionPane.showMessageDialog(clientController.frame, "Authentication failed. Please check your username and password.");
                break;
            case "REGISTER_SUCCESS":
                JOptionPane.showMessageDialog(clientController.frame, "Registration successful. You can now log in.");
                clientController.switchToLoginPanel();
                break;
            case "REGISTER_FAIL":
                JOptionPane.showMessageDialog(clientController.frame, "Registration failed. Username already exists.");
                break;
            case "USER_JOINED":
                String usernameJoined = parts[1];
                clientController.gamePanel.updateUserList(new String[] { usernameJoined });
                break;
            case "USER_LEFT":
                String usernameLeft = parts[1];
                clientController.gamePanel.updateUserList(new String[] { usernameLeft });
                break;
            case "USER_LIST":
                String[] users = parts[1].split(",");
                clientController.gamePanel.updateUserList(users);
                break;
            case "SELECT_CARD":
                handleSelectCardMessage(parts);
                break;
            case "CARD_SELECTED":
                String roomCode = parts[1];
                String username = parts[2];
                String card = parts[3];
                clientController.gamePanel.updateMessage(username + " selected card: " + card);
                break;
            case "NEW_SUM":
                int newSum = Integer.parseInt(parts[1]);
                clientController.generateNewSum(); // Update sum
                break;
            // Handle other messages as needed
        }
    }

    private void handleSelectCardMessage(String[] parts) {
        if (parts.length >= 4) {
            String username = parts[2];
            String card = parts[3];
            String message = username + " selected card: " + card;
            clientController.gamePanel.updateMessage(message);
        }
    }
}
