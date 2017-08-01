package com.evanlennick.webserver.response

import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.GZIPOutputStream


//todo get rid of this and just used default/named arguments in HttpResponse object
class HttpResponseBuilder {

    private var code: HttpResponseCode? = null

    private var headers: MutableMap<String, String> = mutableMapOf()

    private var body: ByteArray? = null

    private var includeBody = true

    private var requestId: UUID? = null

    private var gzipBody: Boolean = false

    fun code(code: HttpResponseCode): HttpResponseBuilder {
        this.code = code
        return this
    }

    fun setHeaders(headers: MutableMap<String, String>): HttpResponseBuilder {
        this.headers = headers
        return this
    }

    fun addHeader(headerFieldName: String, headerValue: String): HttpResponseBuilder {
        this.headers!!.put(headerFieldName, headerValue)
        return this
    }

    fun body(body: ByteArray?): HttpResponseBuilder {
        this.body = body
        return this
    }

    fun gzipBody(): HttpResponseBuilder {
        this.gzipBody = true
        return this
    }

    fun dontIncludeBody(): HttpResponseBuilder {
        this.includeBody = false
        return this
    }

    fun forRequest(id: UUID?): HttpResponseBuilder {
        this.requestId = id
        return this
    }

    fun build(): HttpResponse {
        val response: HttpResponse
        if (includeBody && null != body) {
            if (gzipBody) {
                val compressedBody = compressResponseBody(body)
                val length = Integer.toString(compressedBody.size)
                this.addHeader("Content-Length", length)
                this.addHeader("Content-Encoding", "gzip")
                response = HttpResponse(code!!, headers, compressedBody)
            } else {
                val length = Integer.toString(body!!.size)
                this.addHeader("Content-Length", length)
                response = HttpResponse(code!!, headers, body)
            }
        } else {
            response = HttpResponse(code!!, headers, null)
        }

        if (null != requestId) {
            response.requestId = requestId
        }

        return response
    }

    private fun compressResponseBody(uncompressedBody: ByteArray?): ByteArray {
        val baos = ByteArrayOutputStream()

        val zos = GZIPOutputStream(baos)
        zos.write(uncompressedBody)
        zos.close()

        return baos.toByteArray()
    }
}
