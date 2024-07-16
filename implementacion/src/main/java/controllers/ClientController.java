package controllers;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import models.ClientModel;
import views.ClientView;

import org.bson.Document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ClientController {
    private ClientView view;
    private ClientModel model;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;
    private boolean isLoggedIn = false;

    public ClientController(ClientView view, ClientModel model) {
        this.view = view;
        this.model = model;
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.database = mongoClient.getDatabase("chat_app");
        this.usersCollection = database.getCollection("users");

        // Deshabilitar el área de chat inicialmente
        view.enableChat(false);

        // Agregar listener para el botón de enviar mensaje
        view.addSendButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    String message = view.getInputText();
                    model.sendMessage(message);
                    view.displayMessage("Me: " + message); // Mostrar el mensaje en la vista inmediatamente
                    view.clearInputText();
                } else {
                    JOptionPane.showMessageDialog(view, "Please login to send messages.");
                }
            }
        });

        // Agregar listener para el botón de inicio de sesión
        view.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });

        // Agregar listener para el botón de registro
        view.addRegisterButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }

    public void startClient(String ip, int port) {
        model.connectToServer(ip, port);

        // Iniciar hilo para recibir mensajes del servidor
        startMessageReceiverThread();
    }

    private void startMessageReceiverThread() {
        new Thread(() -> {
            while (true) {
                String message = model.receiveMessage();
                if (message != null) {
                    view.displayMessage(message);
                }
            }
        }).start();
    }

    private void showLoginDialog() {
        String username = JOptionPane.showInputDialog(view, "Enter your username:");
        String password = JOptionPane.showInputDialog(view, "Enter your password:");

        // Verificar la autenticación con MongoDB
        Document user = usersCollection.find(Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password)
        )).first();

        if (user != null) {
            model.setUsername(username);
            view.displayMessage("Logged in as: " + username);
            isLoggedIn = true;
            view.showLoginPanel(false); // Ocultar el panel de login
            view.enableChat(true); // Habilitar el área de chat
        } else {
            view.displayMessage("Invalid credentials. Please try again.");
        }
    }

    private void showRegisterDialog() {
        String username = JOptionPane.showInputDialog(view, "Enter a new username:");
        String password = JOptionPane.showInputDialog(view, "Enter a new password:");

        // Verificar si el usuario ya existe
        Document existingUser = usersCollection.find(Filters.eq("username", username)).first();
        if (existingUser != null) {
            view.displayMessage("Username already exists. Please choose another one.");
            return;
        }

        // Registrar nuevo usuario en MongoDB
        Document newUser = new Document("username", username)
                .append("password", password);
        usersCollection.insertOne(newUser);

        view.displayMessage("User registered successfully: " + username);
    }

    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
