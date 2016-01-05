package com.evanlennick.webserver.mimetypes;

import java.util.Arrays;
import java.util.List;

public enum MimeType {

    IMG_PNG("image/png", Arrays.asList("png")),

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
