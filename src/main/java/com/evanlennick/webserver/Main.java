package com.evanlennick.webserver;

public class Main {

    public static final int DEFAULT_PORT = 8180;

    public static void main(String[] args) {
        final int port = getPort(args);

        Runnable runnable = new Runnable() {
            public void run() {
                Server server = new Server(port);
                server.start();
            }
        };
        runnable.run();
    }

    private static int getPort(String[] args) {
        if (args.length > 0) {
            return Integer.parseInt(args[0]);
        } else {
            return DEFAULT_PORT;
        }
    }

}
