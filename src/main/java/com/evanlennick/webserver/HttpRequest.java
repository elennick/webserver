package com.evanlennick.webserver;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class HttpRequest {

    String requestLine;

    String method;

    String resource;

    String version;

    Map<String, String> headers;

    String body;

    public HttpRequest(String request) throws IOException {
        headers = Maps.newHashMap();
        BufferedReader reader = new BufferedReader(new StringReader(request));

        String line = reader.readLine();
        requestLine = line;
        String[] splitRequestLine = requestLine.split(" ");
        method = splitRequestLine[0].trim();
        resource = splitRequestLine[1].trim();
        version = splitRequestLine[2].trim();

        line = reader.readLine();
        while (null != line && !line.isEmpty()) {
            String[] splitHeader = line.split(":");
            String headerFieldName = splitHeader[0].trim();
            String headerFieldValue = splitHeader[1].trim();
            headers.put(headerFieldName, headerFieldValue);
            line = reader.readLine();
        }

        line = reader.readLine();
        StringBuffer requestBody = new StringBuffer(line + "\n");
        while (null != line && !line.isEmpty()) {
            line = reader.readLine();
            requestBody.append(line + "\n");
        }
        body = requestBody.toString();
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getMethod() {
        return method;
    }

    public String getResource() {
        return resource;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestLine='" + requestLine + '\'' +
                ", method='" + method + '\'' +
                ", resource='" + resource + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
