package ec.edu.utils;

import java.io.IOException;
import java.net.DatagramPacket;

import javax.swing.JOptionPane;

import ec.edu.controllers.ClientController;

public class Listener implements Runnable {
    /**
     *
     */
    private final ClientController clientController;

    /**
     * @param clientController
     */
    public Listener(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                this.clientController.socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                handleServerMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleServerMessage(String message) {
        String[] parts = message.split(":");
        String command = parts[0];

        switch (command) {
            case "ROOM_JOINED":
                JOptionPane.showMessageDialog(this.clientController.frame, "Joined room: " + parts[1]);
                break;
            case "USER_JOINED":
                this.clientController.roomPanel.addUser(parts[1]);
                break;
            case "USER_LEFT":
                this.clientController.roomPanel.removeUser(parts[1]);
                break;
            case "CARD_SELECTED":
                String selectedUser = parts[1];
                String card = parts[2];
                this.clientController.gamePanel.updateMessage(selectedUser + " selected card " + card);
                break;
            case "MESSAGE":
                JOptionPane.showMessageDialog(this.clientController.frame, parts[1]);
                break;
        }
    }
}