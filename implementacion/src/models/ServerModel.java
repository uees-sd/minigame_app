package models;

import java.util.ArrayList;
import java.util.List;

import controllers.ClientHandler;

public class ServerModel {
    private List<ClientHandler> clients = new ArrayList<>();

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
