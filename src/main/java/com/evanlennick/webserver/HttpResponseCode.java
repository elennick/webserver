package com.evanlennick.webserver;

public enum HttpResponseCode {

    OK(200, "OK"),

    NOT_FOUND(404, "Not Found");

    private int statusCode;

    private String reasonPhrase;

    HttpResponseCode(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
