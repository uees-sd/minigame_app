package ec.edu.controllers;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

import ec.edu.utils.ClientInfo;

public class ServerController {
    private static final int SERVER_PORT = 90;

    private DatagramSocket socket;
    private Map<String, List<ClientInfo>> rooms;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    private Map<String, Integer> playerScores = new HashMap<>(); // Track scores
    private Map<String, Integer> currentSums = new HashMap<>(); // Track current sums for each room

    public ServerController() throws IOException {
        socket = new DatagramSocket(SERVER_PORT);
        rooms = new HashMap<>();
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("mathquizdb");
        usersCollection = database.getCollection("users");
    }

    public void start() {
        byte[] buffer = new byte[256];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                handleClientMessage(message, packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClientMessage(String message, InetAddress address, int port) throws IOException {
        System.out.println("Received message: " + message + " from " + address + ":" + port);
        String[] parts = message.split(":");
        String command = parts[0];

        switch (command) {
            case "CREATE_ROOM":
                System.out.println("Create Room Command: RoomCode=" + parts[1] + ", Username=" + parts[2]);
                createRoom(parts[1], address, port, parts[2]);
                break;
            case "JOIN_ROOM":
                System.out.println("Join Room Command: RoomCode=" + parts[1] + ", Username=" + parts[2]);
                joinRoom(parts[1], address, port, parts[2]);
                break;
            case "LEAVE_ROOM":
                System.out.println("Leave Room Command: RoomCode=" + parts[1] + ", Username=" + parts[2]);
                leaveRoom(parts[1], address, port, parts[2]);
                break;
            case "SELECT_CARD":
                System.out.println("Select Card Command: RoomCode=" + parts[1] + ", Username=" + parts[2] + ", Card=" + parts[3]);
                selectCard(parts[1], parts[2], Integer.parseInt(parts[3]), address, port);
                break;
            case "PASS":
                System.out.println("Pass Command: RoomCode=" + parts[1] + ", Username=" + parts[2]);
                pass(parts[1], address, port);
                break;
            case "AUTHENTICATE_USER":
                System.out.println("Authenticate User Command: Username=" + parts[1]);
                authenticateUser(parts[1], parts[2], address, port);
                break;
            case "REGISTER_USER":
                System.out.println("Register User Command: Username=" + parts[1]);
                registerUser(parts[1], parts[2], address, port);
                break;
        }
    }

    private void authenticateUser(String username, String password, InetAddress address, int port) throws IOException {
        Document query = new Document("username", username).append("password", password);
        Document user = usersCollection.find(query).first();

        if (user != null) {
            System.out.println("User authenticated successfully: " + username);
            sendMessage(address, port, "AUTH_SUCCESS");
        } else {
            System.out.println("Authentication failed for user: " + username);
            sendMessage(address, port, "AUTH_FAIL");
        }
    }

    private void registerUser(String username, String password, InetAddress address, int port) throws IOException {
        Document existingUser = usersCollection.find(new Document("username", username)).first();
        if (existingUser != null) {
            System.out.println("Registration failed, username already exists: " + username);
            sendMessage(address, port, "REGISTER_FAIL");
        } else {
            Document newUser = new Document("username", username).append("password", password);
            usersCollection.insertOne(newUser);
            System.out.println("User registered successfully: " + username);
            sendMessage(address, port, "REGISTER_SUCCESS");
        }
    }

    private void createRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        if (username == null || username.isEmpty()) {
            sendMessage(address, port, "CREATE_ROOM_FAIL");
            return;
        }

        if (!rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = new ArrayList<>();
            rooms.put(roomCode, clients);
            System.out.println("Room created: " + roomCode);
            currentSums.put(roomCode, generateNewSum()); // Generate initial sum for the room
        }
        joinRoom(roomCode, address, port, username);
    }

    private void joinRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        if (username == null || username.isEmpty()) {
            sendMessage(address, port, "JOIN_FAIL");
            return;
        }

        if (rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = rooms.get(roomCode);
            ClientInfo client = new ClientInfo(address, port, username);
            clients.add(client);
            System.out.println("User " + username + " joined room: " + roomCode);
            sendMessage(address, port, "JOIN_SUCCESS");

            // Notify other users in the room
            notifyRoom(roomCode, "USER_JOINED:" + username);
            // Send updated list of users
            sendUpdatedUserList(roomCode);
        } else {
            System.out.println("Room does not exist: " + roomCode);
            sendMessage(address, port, "JOIN_FAIL");
        }
    }

    private void leaveRoom(String roomCode, InetAddress address, int port, String username) throws IOException {
        if (username == null || username.isEmpty()) {
            sendMessage(address, port, "LEAVE_FAIL");
            return;
        }

        if (rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = rooms.get(roomCode);
            clients.removeIf(client -> client.getUsername().equals(username) && client.getAddress().equals(address) && client.getPort() == port);
            System.out.println("User " + username + " left room: " + roomCode);
            sendMessage(address, port, "LEAVE_SUCCESS");

            // Notify other users in the room
            notifyRoom(roomCode, "USER_LEFT:" + username);
            // Send updated list of users
            sendUpdatedUserList(roomCode);
        } else {
            System.out.println("Room does not exist: " + roomCode);
            sendMessage(address, port, "LEAVE_FAIL");
        }
    }

    private void selectCard(String roomCode, String username, int cardNumber, InetAddress address, int port) throws IOException {
        if (username == null || username.isEmpty()) {
            sendMessage(address, port, "SELECT_FAIL");
            return;
        }

        if (rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = rooms.get(roomCode);
            int sum = currentSums.get(roomCode);

            if (cardNumber == sum) {
                sendMessage(address, port, "CORRECT:" + cardNumber);
                playerScores.put(username, playerScores.getOrDefault(username, 0) + 1);

                if (playerScores.get(username) == 9) {
                    for (ClientInfo client : clients) {
                        sendMessage(client.getAddress(), client.getPort(), "WINNER:" + username);
                    }
                } else {
                    int newSum = generateNewSum();
                    currentSums.put(roomCode, newSum);
                    notifyRoom(roomCode, "NEW_SUM:" + newSum);
                }
            } else {
                sendMessage(address, port, "WRONG");
            }
        } else {
            sendMessage(address, port, "SELECT_FAIL");
        }
    }

    private void pass(String roomCode, InetAddress address, int port) throws IOException {
        if (rooms.containsKey(roomCode)) {
            int newSum = generateNewSum();
            currentSums.put(roomCode, newSum);
            notifyRoom(roomCode, "NEW_SUM:" + newSum);
        } else {
            sendMessage(address, port, "PASS_FAIL");
        }
    }

    private int generateNewSum() {
        Random random = new Random();
        int a, b;
        do {
            a = random.nextInt(10);
            b = random.nextInt(10);
        } while (a + b < 1 || a + b > 10 || a + b == 8);
        return a + b;
    }

    private void sendMessage(InetAddress address, int port, String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }

    private void notifyRoom(String roomCode, String message) {
        if (rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = rooms.get(roomCode);
            for (ClientInfo client : clients) {
                try {
                    sendMessage(client.getAddress(), client.getPort(), message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendUpdatedUserList(String roomCode) throws IOException {
        if (rooms.containsKey(roomCode)) {
            List<ClientInfo> clients = rooms.get(roomCode);
            StringBuilder userList = new StringBuilder("USER_LIST:");
            for (ClientInfo client : clients) {
                userList.append(client.getUsername()).append(",");
            }
            if (userList.length() > 0) {
                userList.setLength(userList.length() - 1); // Remove trailing comma
            }
            notifyRoom(roomCode, userList.toString());
        }
    }
}
