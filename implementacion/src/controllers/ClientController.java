package controllers;

import java.util.Scanner;

import models.ClientModel;
import views.ClientView;

public class ClientController {
    private ClientView view;
    private ClientModel model;

    public ClientController(ClientView view, ClientModel model) {
        this.view = view;
        this.model = model;
    }

    public void startClient(String ip, int port) {
        model.connectToServer(ip, port);
    
        Scanner scanner = new Scanner(System.in);
    
        // Solicitar el nombre de usuario
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        model.setUsername(username); // Configurar el nombre de usuario en el modelo
    
        while (true) {
            String message = scanner.nextLine();
            model.sendMessage(message);
        }
    }
    

    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
