package com.evanlennick.webserver.mimetypes;

import com.google.common.collect.Maps;

import java.util.Map;

public final class MimeTypeUtil {

    private static Map<String, String> mimeTypeMap;

    private MimeTypeUtil() {}

    public static String getMimeTypeStringByFileExtension(String extension) {
        if (null == mimeTypeMap) {
            initMimeTypeMap();
        }

        return mimeTypeMap.get(extension);
    }

    private static void initMimeTypeMap() {
        mimeTypeMap = Maps.newHashMap();

        MimeType[] values = MimeType.values();
        for (MimeType value : values) {
            for (String extension : value.getExtensions()) {
                mimeTypeMap.put(extension, value.getMimeTypeString());
            }
        }
    }
}
