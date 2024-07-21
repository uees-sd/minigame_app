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

import javax.swing.*;

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
        this.database = mongoClient.getDatabase("quiz_app");
        this.usersCollection = database.getCollection("users");

        view.enableQuestionPanel(false);
        view.showRoomOptions(false);

        view.addAnswerButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLoggedIn) {
                    JButton source = (JButton) e.getSource();
                    String answer = source.getText();
                    model.submitAnswer(answer);
                } else {
                    JOptionPane.showMessageDialog(view, "Please login to play.");
                }
            }
        });

        view.addLoginButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });

        view.addRegisterButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });

        view.addCreateRoomButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.createRoom();
            }
        });

        view.addJoinRoomButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomCode = JOptionPane.showInputDialog(view, "Enter room code:");
                model.joinRoom(roomCode);
            }
        });
    }

    public void startClient(String ip, int port) {
        model.connectToServer(ip, port);
        startMessageReceiverThread();
    }

    private void startMessageReceiverThread() {
        new Thread(() -> {
            while (true) {
                String message = model.receiveMessage();
                if (message != null) {
                    // Parse and handle the received message
                    // Example: "QUESTION|What is 2+2?|3|4|5|6"
                    String[] parts = message.split("\\|");
                    if (parts[0].equals("QUESTION")) {
                        view.setQuestion(parts[1], new String[]{parts[2], parts[3], parts[4], parts[5]});
                        view.enableQuestionPanel(true);
                    }
                }
            }
        }).start();
    }

    private void showLoginDialog() {
        String username = JOptionPane.showInputDialog(view, "Enter your username:");
        String password = JOptionPane.showInputDialog(view, "Enter your password:");

        Document user = usersCollection.find(Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password)
        )).first();

        if (user != null) {
            model.setUsername(username);
            view.displayMessage("Logged in as: " + username);
            isLoggedIn = true;
            view.showLoginPanel(false);
            view.showRoomOptions(true);
        } else {
            view.displayMessage("Invalid credentials. Please try again.");
        }
    }

    private void showRegisterDialog() {
        String username = JOptionPane.showInputDialog(view, "Enter a new username:");
        String password = JOptionPane.showInputDialog(view, "Enter a new password:");

        Document existingUser = usersCollection.find(Filters.eq("username", username)).first();
        if (existingUser != null) {
            view.displayMessage("Username already exists. Please choose another one.");
            return;
        }

        Document newUser = new Document("username", username)
                .append("password", password);
        usersCollection.insertOne(newUser);

        view.displayMessage("User registered successfully: " + username);
    }

    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
