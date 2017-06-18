package com.evanlennick.webserver;

import java.io.IOException;

import static com.evanlennick.webserver.Configuration.DEFAULT_PORT;

public class Main {

    public static void main(String[] args) {
        final int port = getPort(args);

        new Thread(() -> {
            Server server = new Server(port);
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static int getPort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        } else {
            return DEFAULT_PORT;
        }
    }

}
