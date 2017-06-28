package com.evanlennick.webserver.response

import com.google.common.collect.Maps
import java.util.UUID

//todo get rid of this and just used default/named arguments in HttpResponse object
class HttpResponseBuilder {

    private var code: HttpResponseCode? = null

    private var headers: MutableMap<String, String>? = null

    private var body: ByteArray? = null

    private var includeBody = true

    private var requestId: UUID? = null

    init {
        headers = Maps.newHashMap<String, String>()
    }

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
        if (null != body) {
            val length = Integer.toString(body.size)
            this.addHeader("Content-Length", length)
        }
        this.body = body
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
        if (includeBody) {
            response = HttpResponse(code!!, headers, body)
        } else {
            response = HttpResponse(code!!, headers, null)
        }

        if (null != requestId) {
            response.requestId = requestId
        }

        return response
    }
}
