package com.evanlennick.webserver.response;

import com.google.common.collect.Maps;

import java.util.Map;

public class HttpResponseBuilder {

    private HttpResponseCode code;

    private Map<String, String> headers;

    private byte[] body;

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
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(code, headers, body);
    }
}
