package ec.edu.controllers;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

import ec.edu.utils.Listener;
import ec.edu.views.GamePanel;
import ec.edu.views.LoginPanel;
import ec.edu.views.RegisterPanel;
import ec.edu.views.RoomPanel;

public class ClientController {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 90;

    public DatagramSocket socket;
    private InetAddress address;

    public JFrame frame;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    public RoomPanel roomPanel;
    public GamePanel gamePanel;

    private String username;
    private String currentRoomCode;

    public void start() throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(SERVER_ADDRESS);

        frame = new JFrame("Card Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        roomPanel = new RoomPanel(this);
        gamePanel = new GamePanel(this);

        frame.add(loginPanel, "login");
        frame.add(registerPanel, "register");
        frame.add(roomPanel, "room");
        frame.add(gamePanel, "game");

        frame.setSize(800, 600);
        frame.setVisible(true);

        new Thread(new Listener(this)).start();
    }

    public void sendMessage(String message) {
        if (username == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please login first.");
            return;
        }
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, SERVER_PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void switchToLoginPanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "login");
    }

    public void switchToRegisterPanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "register");
    }

    public void switchToRoomPanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "room");
    }

    public void switchToGamePanel() {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), "game");
    }

    public void authenticateUser(String username, String password) {
        this.username = username; // Set username from login
        sendMessage("AUTHENTICATE_USER:" + username + ":" + password);
    }

    public void registerUser(String username, String password) {
        sendMessage("REGISTER_USER:" + username + ":" + password);
    }

    public void createRoom(String roomCode) {
        if (roomCode == null || roomCode.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Room code cannot be empty.");
            return;
        }
        if (username != null && !username.isEmpty()) {
            sendMessage("CREATE_ROOM:" + roomCode + ":" + username);
        } else {
            JOptionPane.showMessageDialog(frame, "Username is not set.");
        }
    }
    
    public void joinRoom(String roomCode) {
        if (roomCode == null || roomCode.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Room code cannot be empty.");
            return;
        }
        if (username != null && !username.isEmpty()) {
            sendMessage("JOIN_ROOM:" + roomCode + ":" + username);
        } else {
            JOptionPane.showMessageDialog(frame, "Username is not set.");
        }
    }
    

    public void leaveRoom(String roomCode) {
        if (username != null && !username.isEmpty()) {
            sendMessage("LEAVE_ROOM:" + roomCode + ":" + username);
        } else {
            JOptionPane.showMessageDialog(frame, "Username is not set.");
        }
    }

    public void selectCard(String roomCode, String card) {
        if (username != null && !username.isEmpty()) {
            sendMessage("SELECT_CARD:" + roomCode + ":" + username + ":" + card);
        } else {
            JOptionPane.showMessageDialog(frame, "Username is not set.");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentRoomCode() {
        return currentRoomCode;
    }

    public void setCurrentRoomCode(String currentRoomCode) {
        this.currentRoomCode = currentRoomCode;
    }
}
