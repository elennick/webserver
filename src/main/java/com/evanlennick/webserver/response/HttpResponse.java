package com.evanlennick.webserver.response;

import com.google.common.primitives.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {

    public static final String HTTP_EOL = "\r\n";

    private String version;

    private HttpResponseCode code;

    private Map<String, String> headers;

    private byte[] body;

    public HttpResponse() {}

    public HttpResponse(HttpResponseCode code, Map<String, String> headers, byte[] body) {
        this.version = "HTTP/1.1";
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public byte[] getResponseAsBytes() {
        StringBuilder header = new StringBuilder(); //todo look into stringbuilder/stringbuffer and see if this is even necessary
        header.append(getStatusLine());
        header.append(HTTP_EOL);
        header.append(getHeadersInHttpFormat());
        header.append(HTTP_EOL);

        byte[] headerAsBytes = header.toString().getBytes(StandardCharsets.UTF_8);

        if (Objects.nonNull(body)) {
            return Bytes.concat(headerAsBytes, body);
        } else {
            return headerAsBytes;
        }
    }

    public String getStatusLine() {
        return version + " " + code.getStatusCode() + " " + code.getReasonPhrase();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeadersInHttpFormat() {
        StringBuffer headersInHttpFormat = new StringBuffer();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headersInHttpFormat.append(header.getKey());
            headersInHttpFormat.append(": ");
            headersInHttpFormat.append(header.getValue());
            headersInHttpFormat.append(HTTP_EOL);
        }
        return headersInHttpFormat.toString();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", code=" + code +
                ", headers=" + headers +
                '}';
    }
}