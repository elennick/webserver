package com.evanlennick.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;

    private boolean running;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error encountered while starting up server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("Listening on port " + port + "...\n");

        running = true;
        while (running) {
            try {
                Socket socket = server.accept();
                synchronized (this) {
                    Client client = new Client(socket);
                    client.handleRequest();
                }
            } catch (IOException e) {
                System.out.println("Error encountered during request: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
