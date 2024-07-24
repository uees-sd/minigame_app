package ec.edu.views;

import javax.swing.*;

import ec.edu.controllers.ClientController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomPanel extends JPanel {
    private ClientController client;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;

    public RoomPanel(ClientController client) {
        this.client = client;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel roomLabel = new JLabel("Enter Room Code:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(roomLabel, gbc);

        JTextField roomField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(roomField, gbc);

        JButton createRoomButton = new JButton("Create Room");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createRoomButton, gbc);

        JButton joinRoomButton = new JButton("Join Room");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(joinRoomButton, gbc);

        JButton leaveRoomButton = new JButton("Leave Room");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(leaveRoomButton, gbc);

        JButton startGameButton = new JButton("Start Game");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(startGameButton, gbc);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScrollPane = new JScrollPane(userList);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(userScrollPane, gbc);

        createRoomButton.addActionListener(e -> {
            String roomCode = roomField.getText().trim();
            if (!roomCode.isEmpty()) {
                client.setCurrentRoomCode(roomCode);
                client.sendMessage("CREATE_ROOM:" + roomCode + ":" + client.getUsername());
            }
        });

        joinRoomButton.addActionListener(e -> {
            String roomCode = roomField.getText().trim();
            if (!roomCode.isEmpty()) {
                client.setCurrentRoomCode(roomCode);
                client.sendMessage("JOIN_ROOM:" + roomCode + ":" + client.getUsername());
            }
        });

        leaveRoomButton.addActionListener(e -> {
            if (client.getCurrentRoomCode() != null) {
                client.sendMessage("LEAVE_ROOM:" + client.getCurrentRoomCode() + ":" + client.getUsername());
                client.setCurrentRoomCode(null);
                userListModel.clear();
            }
        });

        startGameButton.addActionListener(e -> client.switchToGamePanel());
    }

    public void addUser(String username) {
        userListModel.addElement(username);
    }

    public void removeUser(String username) {
        userListModel.removeElement(username);
    }
}
