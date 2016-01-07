package com.evanlennick.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    private final int SOCKET_TIMEOUT_IN_SECONDS = 30;

    private final int port;

    private ExecutorService pool;

    private boolean running;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
            pool = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            System.out.println("Error encountered while starting up server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("Listening on port " + port + "...\n");
        final int socketTimeout = new Long(Duration.ofSeconds(SOCKET_TIMEOUT_IN_SECONDS).toMillis()).intValue();

        running = true;
        while (running) {
            final Socket socket = server.accept();
            synchronized (this) {
                pool.execute(new Runnable() {
                    public void run() {
                        try {
                            socket.setSoTimeout(socketTimeout);
                            RequestHandler requestHandler = new RequestHandler(socket);
                            requestHandler.go();
                        } catch (IOException e) {
                            System.out.println("Error encountered during request: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        shutdownAndAwaitTermination(pool);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
