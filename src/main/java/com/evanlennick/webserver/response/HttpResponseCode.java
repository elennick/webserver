package com.evanlennick.webserver.response;

public enum HttpResponseCode {

    OK(200, "OK"),

    ACCEPTED(202, "Accepted"),

    NO_CONTENT(204, "No Content"),

    BAD_REQUEST(400, "Bad Request"),

    NOT_FOUND(404, "Not Found"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

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
