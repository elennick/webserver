package com.evanlennick.webserver;

import com.evanlennick.webserver.mimetypes.MimeType;
import com.evanlennick.webserver.mimetypes.MimeTypeUtil;
import com.evanlennick.webserver.request.HttpRequest;
import com.evanlennick.webserver.response.HttpResponse;
import com.evanlennick.webserver.response.HttpResponseBuilder;
import com.evanlennick.webserver.response.HttpResponseCode;
import com.google.common.io.Files;

import java.io.*;
import java.net.Socket;

import static com.evanlennick.webserver.Constants.HTTP_EOL;

public class RequestHandler {

    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void go() throws IOException {
        long start = System.currentTimeMillis();

        String requestString = readRequest(socket);
        HttpRequest request = new HttpRequest(requestString);
        System.out.println(request);

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
                    .forRequest(request.getRequestId())
                    .build();
        }
        System.out.println(response);
        writeResponse(response);

        socket.close();

        long stop = System.currentTimeMillis();
        long elapsed = stop - start;
        System.out.println("Request " + request.getRequestId() + " took " + elapsed + "ms\n\n");
    }

    private String readRequest(Socket socket) throws IOException {
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);

        String line = reader.readLine();
        StringBuffer request = new StringBuffer(line + HTTP_EOL);
        while (!line.isEmpty()) {
            line = reader.readLine();
            request.append(line + HTTP_EOL);
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

            if (!file.exists() || file.isDirectory()) {
                return new HttpResponseBuilder()
                        .code(HttpResponseCode.NOT_FOUND)
                        .forRequest(request.getRequestId())
                        .build();
            }

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
                .forRequest(request.getRequestId())
                .body(body);

        if (request.isHeadRequest()) {
            responseBuilder.dontIncludeBody();
        }

        return responseBuilder.build();
    }

    private HttpResponse generateDeleteResponse(HttpRequest request) {
        return new HttpResponseBuilder()
                .code(HttpResponseCode.ACCEPTED)
                .forRequest(request.getRequestId())
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
