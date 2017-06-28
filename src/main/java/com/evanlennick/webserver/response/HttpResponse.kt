package com.evanlennick.webserver.response

import com.evanlennick.webserver.Constants.HTTP_EOL
import com.evanlennick.webserver.Constants.SERVER_STRING
import com.evanlennick.webserver.Constants.SYSTEM_EOL
import com.evanlennick.webserver.Utils
import com.google.common.collect.Maps
import com.google.common.primitives.Bytes
import java.nio.charset.StandardCharsets
import java.util.*

class HttpResponse(private val code: HttpResponseCode, private var headers: MutableMap<String, String>?, var body: ByteArray?) {
    private val version: String = "HTTP/1.1"

    var requestId: UUID? = null

    init {

        addHeader("Date", Utils.rfc1123FormattedDateTime)
        addHeader("Server", SERVER_STRING)
    }

    val responseAsBytes: ByteArray
        get() {
            val headerAsBytes = responseHeader.toByteArray(StandardCharsets.UTF_8)

            if (Objects.nonNull(body)) {
                return Bytes.concat(headerAsBytes, body)
            } else {
                return headerAsBytes
            }
        }

    val responseHeader: String
        get() {
            val header = StringBuilder()
            header.append(statusLine)
            header.append(HTTP_EOL)
            header.append(headersInHttpFormat)
            header.append(HTTP_EOL)

            return header.toString()
        }

    val statusLine: String
        get() = version + " " + code.statusCode + " " + code.reasonPhrase

    fun getHeaders(): Map<String, String> {
        return headers!!
    }

    val headersInHttpFormat: String
        get() {
            val headersInHttpFormat = StringBuffer()
            for ((key, value) in headers!!) {
                headersInHttpFormat.append(key)
                headersInHttpFormat.append(": ")
                headersInHttpFormat.append(value)
                headersInHttpFormat.append(HTTP_EOL)
            }
            return headersInHttpFormat.toString()
        }

    fun addHeader(headerFieldName: String, headerValue: String) {
        if (null == headers) {
            headers = Maps.newHashMap<String, String>()
        }

        headers!!.put(headerFieldName, headerValue)
    }

    fun setHeaders(headers: MutableMap<String, String>) {
        this.headers = headers
    }

    override fun toString(): String {
        var requestText = "RESPONSE FOR REQUEST ID: " + requestId + SYSTEM_EOL
        requestText += responseHeader

        return requestText
    }
}
