package ec.edu.utils;

import java.net.InetAddress;

public class ClientInfo {
    private InetAddress address;
    private int port;
    private String username;

    public ClientInfo(InetAddress address, int port, String username) {
        this.address = address;
        this.port = port;
        this.username = username;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }
}
