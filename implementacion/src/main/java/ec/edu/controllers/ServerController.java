package ec.edu.controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

import ec.edu.utils.ClientInfo;

public class ServerController {
    private static final int SERVER_PORT = 90;

    private DatagramSocket socket;
    private Map<String, List<ClientInfo>> rooms;


    public ServerController() throws IOException {
        socket = new DatagramSocket(SERVER_PORT);
        rooms = new HashMap<>();
    }

    public void start() {
        byte[] buffer = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                handleClientMessage(message, packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientMessage(String message, InetAddress address, int port) throws IOException {
        String[] parts = message.split(":");
        String command = parts[0];
        String roomCode = parts[1];
        String username = parts[2];

        switch (command) {
            case "CREATE_ROOM":
                createRoom(roomCode, address, port, username);
                break;
            case "JOIN_ROOM":
                joinRoom(roomCode, address, port, username);
                break;
            case "LEAVE_ROOM":
                leaveRoom(roomCode, address, port, username);
                break;
            case "SELECT_CARD":
                String card = parts[3];
                selectCard(roomCode, username, card);
                break;
        }
    }

    private void createRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        if (!rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = new ArrayList<>();
            rooms.put(roomCode, clients);
        }
        joinRoom(roomCode, address, port, username);
    }

    private void joinRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        List<ClientInfo> clients = rooms.get(roomCode);
        if (clients == null) {
            sendMessage(address, port, "MESSAGE:Room does not exist");
            return;
        }

        for (ClientInfo client : clients) {
            if (client.getUsername().equals(username)) {
                sendMessage(address, port, "MESSAGE:Username already in the room");
                return;
            }
        }

        clients.add(new ClientInfo(address, port, username));
        sendMessage(address, port, "ROOM_JOINED:" + roomCode);

        for (ClientInfo client : clients) {
            sendMessage(client.getAddress(), client.getPort(), "USER_JOINED:" + username);
            if (!client.getUsername().equals(username)) {
                sendMessage(address, port, "USER_JOINED:" + client.getUsername());
            }
        }
    }

    private void leaveRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        List<ClientInfo> clients = rooms.get(roomCode);
        if (clients == null) {
            sendMessage(address, port, "MESSAGE:Room does not exist");
            return;
        }

        clients.removeIf(client -> client.getUsername().equals(username));

        for (ClientInfo client : clients) {
            sendMessage(client.getAddress(), client.getPort(), "USER_LEFT:" + username);
        }

        if (clients.isEmpty()) {
            rooms.remove(roomCode);
        } else {
            sendMessage(address, port, "ROOM_LEFT:" + roomCode);
        }
    }

    private void selectCard(String roomCode, String username, String card) throws IOException {
        List<ClientInfo> clients = rooms.get(roomCode);
        if (clients == null) {
            return;
        }

        for (ClientInfo client : clients) {
            sendMessage(client.getAddress(), client.getPort(), "CARD_SELECTED:" + username + ":" + card);
        }
    }

    private void sendMessage(InetAddress address, int port, String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}
