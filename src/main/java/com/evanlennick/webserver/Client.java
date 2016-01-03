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
        long start = System.currentTimeMillis();

        String requestString = readRequest(socket);
        HttpRequest request = new HttpRequest(requestString);
        System.out.println("request = " + request);

        String response = "\\\n" +
                "HTTP/1.1 200 OK\n" +
                "Date: " + getRfc1123FormattedDateTime() + "\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: elennick-webserver\n" +
                "\n" +
                "<html><title>Test Title!</title><body>Test Body!</body></html>";

        writeResponse(socket, response);

        socket.close();

        long stop = System.currentTimeMillis();
        long elapsed = stop - start;
        System.out.println("Request took " + elapsed + "ms\n\n");
    }

    private String readRequest(Socket socket) throws IOException {
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        String line = reader.readLine();
        StringBuffer request = new StringBuffer(line + "\n");
        while (!line.isEmpty()) {
            line = reader.readLine();
            request.append(line + "\n");
        }

        return request.toString();
    }

    private void writeResponse(Socket socket, String response) throws IOException {
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
