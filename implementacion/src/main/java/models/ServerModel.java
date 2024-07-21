package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import controllers.ClientHandler;

public class ServerModel {
    private Map<String, List<ClientHandler>> rooms = new HashMap<>();

    public void addClient(ClientHandler client, String roomCode) {
        rooms.computeIfAbsent(roomCode, k -> new ArrayList<>()).add(client);
    }

    public void removeClient(ClientHandler client, String roomCode) {
        List<ClientHandler> clients = rooms.get(roomCode);
        if (clients != null) {
            clients.remove(client);
            if (clients.isEmpty()) {
                rooms.remove(roomCode);
            }
        }
    }

    public void broadcastMessage(String message, String roomCode, ClientHandler sender) {
        List<ClientHandler> clients = rooms.get(roomCode);
        if (clients != null) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
}
