package com.evanlennick.webserver.response;

import com.evanlennick.webserver.Utils;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.evanlennick.webserver.Constants.HTTP_EOL;
import static com.evanlennick.webserver.Constants.SYSTEM_EOL;

public class HttpResponse {

    public static final String SERVER_STRING = "elennick-webserver";

    private String version;

    private HttpResponseCode code;

    private Map<String, String> headers;

    private byte[] body;

    private UUID requestId;

    public HttpResponse(HttpResponseCode code, Map<String, String> headers, @Nullable byte[] body) {
        this.version = "HTTP/1.1";
        this.code = code;
        this.headers = headers;
        this.body = body;

        addHeader("Date", Utils.getRfc1123FormattedDateTime());
        addHeader("Server", SERVER_STRING);
    }

    public byte[] getResponseAsBytes() {
        byte[] headerAsBytes = getResponseHeader().getBytes(StandardCharsets.UTF_8);

        if (Objects.nonNull(body)) {
            return Bytes.concat(headerAsBytes, body);
        } else {
            return headerAsBytes;
        }
    }

    public String getResponseHeader() {
        StringBuilder header = new StringBuilder(); //todo look into stringbuilder/stringbuffer and see if this is even necessary
        header.append(getStatusLine());
        header.append(HTTP_EOL);
        header.append(getHeadersInHttpFormat());
        header.append(HTTP_EOL);

        return header.toString();
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

    public void addHeader(String headerFieldName, String headerValue) {
        if (null == headers) {
            headers = Maps.newHashMap();
        }

        headers.put(headerFieldName, headerValue);
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

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        String requestText = "RESPONSE FOR REQUEST ID: " + requestId + SYSTEM_EOL;
        requestText += getResponseHeader();

        return requestText;
    }
}
