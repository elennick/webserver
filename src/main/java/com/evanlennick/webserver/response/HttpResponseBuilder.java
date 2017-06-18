package com.evanlennick.webserver.response;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

public class HttpResponseBuilder {

    private HttpResponseCode code;

    private Map<String, String> headers;

    private byte[] body;

    private boolean includeBody = true;

    private UUID requestId;

    public HttpResponseBuilder() {
        headers = Maps.newHashMap();
    }

    public HttpResponseBuilder code(HttpResponseCode code) {
        this.code = code;
        return this;
    }

    public HttpResponseBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpResponseBuilder addHeader(String headerFieldName, String headerValue) {
        this.headers.put(headerFieldName, headerValue);
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
        if(null != body) {
            String length = Integer.toString(body.length);
            this.addHeader("Content-Length", length);
        }
        this.body = body;
        return this;
    }

    public HttpResponseBuilder dontIncludeBody() {
        this.includeBody = false;
        return this;
    }

    public HttpResponseBuilder forRequest(UUID id) {
        this.requestId = id;
        return this;
    }

    public HttpResponse build() {
        HttpResponse response;
        if(includeBody) {
            response = new HttpResponse(code, headers, body);
        } else {
            response = new HttpResponse(code, headers, null);
        }

        if(null != requestId) {
            response.setRequestId(requestId);
        }

        return response;
    }
}
