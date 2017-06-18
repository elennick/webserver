package com.evanlennick.webserver.request;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.UUID;

import static com.evanlennick.webserver.Constants.SYSTEM_EOL;

public class HttpRequest {

    private UUID requestId;

    private String requestLine;

    private String method;

    private String resource;

    private String version;

    private Map<String, String> headers;

    private String body;

    public HttpRequest(String request) throws IOException {
        requestId = UUID.randomUUID();

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
        if (null != line) {
            StringBuffer requestBody = new StringBuffer(line + "\n");
            while (null != line && !line.isEmpty()) {
                line = reader.readLine();
                requestBody.append(line + "\n");
            }
            body = requestBody.toString();
        }
    }

    public boolean hasRequestMethod(HttpRequestMethod httpRequestMethod) {
        return getMethod().equals(httpRequestMethod.name());
    }

    public boolean isGetRequest() {
        return hasRequestMethod(HttpRequestMethod.GET);
    }

    public boolean isHeadRequest() {
        return hasRequestMethod(HttpRequestMethod.HEAD);
    }

    public boolean isDeleteRequest() {
        return hasRequestMethod(HttpRequestMethod.DELETE);
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

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        String requestText = "REQUEST ID: " + requestId + SYSTEM_EOL;

        requestText += requestLine + SYSTEM_EOL;

        String headersText = "";
        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestText += header.getKey() + ": " + header.getValue() + SYSTEM_EOL;
        }
        requestText += headersText + SYSTEM_EOL;

        if (!Strings.isNullOrEmpty(getBody())) {
            requestText += getBody();
        }

        return requestText;
    }
}
