package com.evanlennick.webserver.mimetypes;

import java.util.Arrays;
import java.util.List;

public enum MimeType {

    IMG_PNG("image/png", Arrays.asList("png", "x-png")),

    IMG_JPG("image/jpeg", Arrays.asList("jpg", "jpeg", "jpe", "jfif", "jfif-tbnl")),

    TEXT_PLAIN("text/plain; charset=UTF-8", Arrays.asList("txt", "text", "c", "c++", "cc", "conf", "java", "log", "pl")),

    TEXT_HTML("text/html; charset=utf-8", Arrays.asList("html", "htm", "shtml", "htmls", "htx", "acgi"));

    private String mimeTypeString;

    private List<String> extensions;

    MimeType(String mimeTypeString, List<String> extensions) {
        this.mimeTypeString = mimeTypeString;
        this.extensions = extensions;
    }

    public String getMimeTypeString() {
        return mimeTypeString;
    }

    public List<String> getExtensions() {
        return extensions;
    }
}
