package com.evanlennick.webserver;

import com.evanlennick.webserver.mimetypes.MimeType;
import com.evanlennick.webserver.mimetypes.MimeTypeUtil;
import com.evanlennick.webserver.request.HttpRequest;
import com.evanlennick.webserver.request.HttpRequestMethod;
import com.evanlennick.webserver.response.HttpResponse;
import com.evanlennick.webserver.response.HttpResponseBuilder;
import com.evanlennick.webserver.response.HttpResponseCode;
import com.google.common.io.Files;

import java.io.*;
import java.net.Socket;

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

        HttpResponse response;
        try {
            if (request.isGetRequest() || request.isHeadRequest()) {
                response = generateGetOrHeadResponse(request);
            } else if (request.isDeleteRequest()) {
                response = generateDeleteResponse(request);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new HttpResponseBuilder()
                    .code(HttpResponseCode.INTERNAL_SERVER_ERROR)
                    .build();
        }
        System.out.println("response = " + response);
        writeResponse(response);

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

    private HttpResponse generateGetOrHeadResponse(HttpRequest request) throws IOException {
        String fileLocation = "www/" + request.getResource();

        ClassLoader classLoader = getClass().getClassLoader();
        String locationRequested;
        byte[] body = null;
        HttpResponseCode code;
        String contentType;

        try {
            locationRequested = classLoader.getResource(fileLocation).getFile();
            File file = determineResourceToReturn(locationRequested);

            code = HttpResponseCode.OK;

            String fileExtension = Files.getFileExtension(file.getAbsolutePath());
            contentType = MimeTypeUtil.getMimeTypeStringByFileExtension(fileExtension);

            body = new byte[(int) file.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(body, 0, body.length);
        } catch (NullPointerException e) {
            code = HttpResponseCode.NOT_FOUND;
            contentType = MimeType.TEXT_PLAIN.getMimeTypeString();
        }

        HttpResponseBuilder responseBuilder = new HttpResponseBuilder()
                .code(code)
                .addHeader("Content-Type", contentType)
                .body(body);

        if(request.isHeadRequest()) {
            responseBuilder.dontIncludeBody();
        }

        return responseBuilder.build();
    }

    private HttpResponse generateDeleteResponse(HttpRequest request) {
        return new HttpResponseBuilder()
                .code(HttpResponseCode.ACCEPTED)
                .build();
    }

    private File determineResourceToReturn(String locationRequested) {
        File file = new File(locationRequested);

        if (file.isDirectory()) {
            File indexFile = new File(file, "index.html");
            if (indexFile.exists() && !indexFile.isDirectory()) {
                file = indexFile;
            }
        }

        return file;
    }

    private void writeResponse(HttpResponse response) throws IOException {
        byte[] responseByteArray = response.getResponseAsBytes();
        OutputStream os = socket.getOutputStream();
        os.write(responseByteArray, 0, responseByteArray.length);
        os.flush();
    }

}
