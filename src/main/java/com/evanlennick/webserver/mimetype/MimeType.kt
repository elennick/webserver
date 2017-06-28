package com.evanlennick.webserver.mimetype

import java.util.Arrays

enum class MimeType private constructor(val mimeTypeString: String, val extensions: List<String>) {

    IMG_PNG("image/png", Arrays.asList("png", "x-png")),

    IMG_JPG("image/jpeg", Arrays.asList("jpg", "jpeg", "jpe", "jfif", "jfif-tbnl")),

    IMG_ICO("image/x-icon", Arrays.asList("ico")),

    APPLICATION_JSON("application/json", Arrays.asList("json")),

    APPLICATION_XML("application/xml", Arrays.asList("xml")),

    APPLICATION_JAVASCRIPT("application/javascript", Arrays.asList("js")),

    TEXT_PLAIN("text/plain; charset=UTF-8", Arrays.asList("txt", "text", "c", "c++", "cc", "conf", "java", "log", "pl")),

    TEXT_HTML("text/html; charset=utf-8", Arrays.asList("html", "htm", "shtml", "htmls", "htx", "acgi"))
}
