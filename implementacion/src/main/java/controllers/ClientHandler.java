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
    private String currentQuestion;
    private String[] currentAnswers;

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
                String[] parts = inputLine.split("\\|", 4);
                if (parts.length < 2) continue;

                String command = parts[0];
                String roomCode = parts.length > 1 ? parts[1] : "";
                String username = parts.length > 2 ? parts[2] : "";
                String message = parts.length > 3 ? parts[3] : "";

                switch (command) {
                    case "CREATE_ROOM":
                        handleCreateRoom(username);
                        break;
                    case "JOIN_ROOM":
                        handleJoinRoom(roomCode, username);
                        break;
                    case "ANSWER":
                        handleAnswer(roomCode, username, message);
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
        sendMessage("QUESTION|What is 2+2?|3|4|5|6");
    }

    private void handleJoinRoom(String roomCode, String username) {
        this.roomCode = roomCode;
        model.addClient(this, roomCode);
        sendMessage("Joined room: " + roomCode);
    }

    private void handleAnswer(String roomCode, String username, String answer) {
        // Validate answer and broadcast result
        model.broadcastMessage(username + " answered: " + answer, roomCode, this);
    }

    private String generateRoomCode() {
        return String.format("%04d", (int)(Math.random() * 10000));
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
