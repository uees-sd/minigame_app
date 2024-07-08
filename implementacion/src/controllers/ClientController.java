package controllers;

import models.ClientModel;
import views.ClientView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ClientController {
    private ClientView view;
    private ClientModel model;

    public ClientController(ClientView view, ClientModel model) {
        this.view = view;
        this.model = model;

        view.addSendButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = view.getInputText();
                model.sendMessage(message);
                view.displayMessage("Me: " + message); // Mostrar el mensaje en la vista inmediatamente
                view.clearInputText();
            }
        });
    }

    public void startClient(String ip, int port) {
        model.connectToServer(ip, port);

        // Solicitar el nombre de usuario
        String username = JOptionPane.showInputDialog(view, "Enter your username:");
        model.setUsername(username);

        // Start a separate thread for receiving messages from the server
        new Thread(() -> {
            while (true) {
                String message = model.receiveMessage();
                if (message != null) {
                    view.displayMessage(message);
                }
            }
        }).start();
    }

    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
