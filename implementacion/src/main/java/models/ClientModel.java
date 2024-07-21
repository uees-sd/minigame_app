package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientModel {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String roomCode;

    public void setUsername(String username) {
        this.username = username;
    }

    public void connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void submitAnswer(String answer) {
        out.println("ANSWER|" + roomCode + "|" + username + "|" + answer);
    }

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createRoom() {
        out.println("CREATE_ROOM|" + username);
    }

    public void joinRoom(String roomCode) {
        this.roomCode = roomCode;
        out.println("JOIN_ROOM|" + roomCode + "|" + username);
    }
}
