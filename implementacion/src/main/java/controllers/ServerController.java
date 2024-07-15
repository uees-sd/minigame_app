package controllers;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import models.ServerModel;
import views.ServerView;

public class ServerController {
    private ServerView view;
    private ServerModel model;

    public ServerController(ServerView view, ServerModel model) {
        this.view = view;
        this.model = model;
    }

    public void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for incoming connections

                // Create a new ClientHandler for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, model, this);
                model.addClient(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
