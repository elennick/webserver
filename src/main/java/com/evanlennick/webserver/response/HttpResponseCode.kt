package com.evanlennick.webserver.response

enum class HttpResponseCode private constructor(val statusCode: Int, val reasonPhrase: String) {

    OK(200, "OK"),

    ACCEPTED(202, "Accepted"),

    NO_CONTENT(204, "No Content"),

    BAD_REQUEST(400, "Bad Request"),

    NOT_FOUND(404, "Not Found"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    NOT_IMPLEMENTED(501, "Not Implemented")
}
