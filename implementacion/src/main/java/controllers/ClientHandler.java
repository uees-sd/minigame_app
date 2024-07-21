package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import models.ServerModel;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private ServerModel model;
    private ServerController controller;
    private PrintWriter out;
    private BufferedReader in;
    private String roomCode;

    public ClientHandler(Socket socket, ServerModel model, ServerController controller) {
        this.clientSocket = socket;
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] parts = inputLine.split("\\|", 3);
                if (parts.length < 2) continue;

                String command = parts[0];
                String message = parts.length > 2 ? parts[2] : "";

                switch (command) {
                    case "CREATE_ROOM":
                        handleCreateRoom(parts[1]);
                        break;
                    case "JOIN_ROOM":
                        handleJoinRoom(parts[1], parts[2]);
                        break;
                    case "MESSAGE":
                        handleMessage(parts[1], message);
                        break;
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateRoom(String username) {
        String roomCode = generateRoomCode();
        model.addClient(this, roomCode);
        sendMessage("Room created with code: " + roomCode);
    }

    private void handleJoinRoom(String roomCode, String username) {
        this.roomCode = roomCode;
        model.addClient(this, roomCode);
        sendMessage("Joined room: " + roomCode);
    }

    private void handleMessage(String roomCode, String message) {
        model.broadcastMessage(message, roomCode, this);
    }

    private String generateRoomCode() {
        return String.format("%04d", (int)(Math.random() * 10000));
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
