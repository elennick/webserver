package com.evanlennick.webserver;

import java.io.*;
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

        writeResponse(socket, request);

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

    private void writeResponse(Socket socket, HttpRequest request) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        String statusLine;
        boolean isOk;
        try {
            String fileLocation = "www/" + request.getResource();
            file = new File(classLoader.getResource(fileLocation).getFile());
            isOk = true;
            statusLine = "HTTP/1.1 200 OK";
        } catch(NullPointerException e) {
            isOk = false;
            statusLine = "HTTP/1.1 404 Not Found";
        }

        String responseHeader = "\\\n" +
                statusLine + "\n" +
                "Date: " + getRfc1123FormattedDateTime() + "\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: elennick-webserver\n" +
                "\n";

        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        osw.write(responseHeader, 0, responseHeader.length());

        if(isOk) {
            byte[] mybytearray = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = socket.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
        } else {
            osw.flush();
        }

//        socket.getOutputStream().flush();

        System.out.println("*** RESPONSE:");
        System.out.println(responseHeader);
        System.out.println();
    }

    private String getRfc1123FormattedDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

}
