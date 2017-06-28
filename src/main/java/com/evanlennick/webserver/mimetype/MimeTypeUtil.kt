package com.evanlennick.webserver.mimetype

import com.google.common.collect.Maps

object MimeTypeUtil {

    private var mimeTypeMap: MutableMap<String, String>? = null

    fun getMimeTypeStringByFileExtension(extension: String): String {
        if (null == mimeTypeMap) {
            initMimeTypeMap()
        }

        var mimeType: String? = mimeTypeMap!![extension]
        if (null == mimeType) {
            mimeType = "application/octet-stream"
        }

        return mimeType
    }

    private fun initMimeTypeMap() {
        mimeTypeMap = Maps.newHashMap<String, String>()

        val values = MimeType.values()
        for (value in values) {
            for (extension in value.extensions) {
                mimeTypeMap!!.put(extension, value.mimeTypeString)
            }
        }
    }
}
