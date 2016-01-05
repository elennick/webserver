package com.evanlennick.webserver.mimetypes;

import java.util.Arrays;
import java.util.List;

public enum MimeType {

    IMG_PNG("image/png", Arrays.asList("png")),

    IMG_JPG("image/jpeg", Arrays.asList("jpg", "jpeg")),

    TEXT_PLAIN("text/plain; charset=UTF-8", Arrays.asList("txt", "text", "c", "c++", "cc", "conf", "java", "log", "pl")),

    TEXT_HTML("text/html; charset=utf-8", Arrays.asList("html", "htm"));

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
