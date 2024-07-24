package ec.edu;

import java.io.IOException;

import ec.edu.controllers.ClientController;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        new ClientController().start();
    }
}
