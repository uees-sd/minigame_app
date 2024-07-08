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
                controller.displayMessage(inputLine);
                model.broadcastMessage(inputLine, this);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
