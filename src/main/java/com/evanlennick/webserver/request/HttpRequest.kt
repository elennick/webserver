package com.evanlennick.webserver.request

import com.evanlennick.webserver.exception.UnableToParseRequestException
import com.google.common.base.Strings
import com.google.common.collect.Maps

import java.io.BufferedReader
import java.io.StringReader
import java.util.UUID

import com.evanlennick.webserver.Constants.SYSTEM_EOL

class HttpRequest(request: String) {

    var requestId: UUID? = null

    var requestLine: String? = null
        private set

    var method: String? = null
        private set

    var resource: String? = null
        private set

    var version: String? = null
        private set

    private var headers: MutableMap<String, String>? = null

    var body: String? = null
        private set

    init {
        try {
            headers = Maps.newHashMap<String, String>()
            val reader = BufferedReader(StringReader(request))

            var line: String? = reader.readLine()
            requestLine = line
            val splitRequestLine = requestLine!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            method = splitRequestLine[0].trim { it <= ' ' }
            resource = splitRequestLine[1].trim { it <= ' ' }
            version = splitRequestLine[2].trim { it <= ' ' }

            line = reader.readLine()
            while (null != line && !line.isEmpty()) {
                val splitHeader = line.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val headerFieldName = splitHeader[0].trim { it <= ' ' }
                val headerFieldValue = splitHeader[1].trim { it <= ' ' }
                headers!!.put(headerFieldName, headerFieldValue)
                line = reader.readLine()
            }

            line = reader.readLine()
            if (null != line) {
                val requestBody = StringBuffer(line + "\n")
                while (null != line && !line.isEmpty()) {
                    line = reader.readLine()
                    requestBody.append(line!! + "\n")
                }
                body = requestBody.toString()
            }
        } catch (e: Exception) {
            throw UnableToParseRequestException(e)
        }

    }

    fun hasRequestMethod(httpRequestMethod: HttpRequestMethod): Boolean {
        return method == httpRequestMethod.name
    }

    val isGetRequest: Boolean
        get() = hasRequestMethod(HttpRequestMethod.GET)

    val isHeadRequest: Boolean
        get() = hasRequestMethod(HttpRequestMethod.HEAD)

    val isDeleteRequest: Boolean
        get() = hasRequestMethod(HttpRequestMethod.DELETE)

    fun getHeaders(): Map<String, String> {
        return headers!!
    }

    override fun toString(): String {
        var requestText = "REQUEST WITH ID: " + requestId + SYSTEM_EOL

        requestText += requestLine!! + SYSTEM_EOL

        val headersText = ""
        for ((key, value) in headers!!) {
            requestText += key + ": " + value + SYSTEM_EOL
        }
        requestText += headersText + SYSTEM_EOL

        if (!Strings.isNullOrEmpty(body)) {
            requestText += body
        }

        return requestText
    }
}
