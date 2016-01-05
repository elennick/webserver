package com.evanlennick.webserver;

import com.evanlennick.webserver.mimetypes.MimeTypeUtil;
import com.evanlennick.webserver.request.HttpRequest;
import com.evanlennick.webserver.response.HttpResponse;
import com.evanlennick.webserver.response.HttpResponseBuilder;
import com.evanlennick.webserver.response.HttpResponseCode;
import com.google.common.io.Files;

import java.io.*;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RequestHandler {

    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void go() throws IOException {
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
        StringBuffer request = new StringBuffer(line + HttpResponse.HTTP_EOL);
        while (!line.isEmpty()) {
            line = reader.readLine();
            request.append(line + HttpResponse.HTTP_EOL);
        }

        return request.toString();
    }

    private void writeResponse(Socket socket, HttpRequest request) throws IOException {
        String fileLocation = "www/" + request.getResource();

        ClassLoader classLoader = getClass().getClassLoader();
        String resource;
        byte[] body = null;
        HttpResponseCode code;
        String contentType = null;

        try {
            resource = classLoader.getResource(fileLocation).getFile();
            File file = new File(resource);

            code = HttpResponseCode.OK;

            String fileExtension = Files.getFileExtension(resource);
            contentType = MimeTypeUtil.getMimeTypeStringByFileExtension(fileExtension);

            body = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(body, 0, body.length);
        } catch (NullPointerException e) {
            code = HttpResponseCode.NOT_FOUND;
        }

        HttpResponseBuilder responseBuilder = new HttpResponseBuilder()
                .code(code)
                .addHeader("Date", getRfc1123FormattedDateTime())
                .addHeader("Server", "elennick-webserver")
                .body(body);

        if(null != contentType) {
            responseBuilder.addHeader("Content-Type", contentType);
        }
        HttpResponse response =  responseBuilder.build();

        System.out.println("response = " + response);

        byte[] responseByteArray = response.getResponseAsBytes();
        OutputStream os = socket.getOutputStream();
        os.write(responseByteArray, 0, responseByteArray.length);

        os.flush();
//        socket.getOutputStream().flush();
    }

    private String getRfc1123FormattedDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

}
