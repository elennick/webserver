package com.evanlennick.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

    private Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void handleRequest() throws IOException {
        System.out.println("*** REQUEST:");
        System.out.println(socket + "\n");

        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        String line = reader.readLine();

        while (!line.isEmpty()) {
            System.out.println(line);
            line = reader.readLine();
        }
        System.out.println();

        String response = "\\\n" +
                "HTTP/1.1 200 OK\n" +
                "Date: " + getRfc1123FormattedDateTime() + "\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: elennick-webserver\n" +
                "\n" +
                "<html><title>Test Title!</title><body>Test Body!</body></html>\n";

        writeResponse(response, socket);

        socket.close();
    }

    private void writeResponse(String response, Socket socket) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(response, 0, response.length());
        osw.flush();

        System.out.println("*** RESPONSE:");
        System.out.println(response);
        System.out.println();
    }

    private String getRfc1123FormattedDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

}
