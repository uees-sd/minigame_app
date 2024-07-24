package ec.edu;

import java.io.IOException;

import ec.edu.controllers.ServerController;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new ServerController().start();
    }
}
