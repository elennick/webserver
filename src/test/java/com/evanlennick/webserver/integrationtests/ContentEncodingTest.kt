package com.evanlennick.webserver.integrationtests

import com.evanlennick.webserver.WebserverIntegrationTestBase
import org.apache.http.client.methods.HttpGet
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ContentEncodingTest : WebserverIntegrationTestBase() {

    @Test fun testNoEncoding() {
        val request = HttpGet(baseUrl + "test.html")
        request.setHeader("Accept-Encoding", "none")
        val response = client.execute(request)

        assertThat(response.statusLine.statusCode).isEqualTo(200)
        assertThat(response.entity.contentEncoding).isNull()
    }

    @Test fun testGzipEncoding() {
        val request = HttpGet(baseUrl + "test.html")
        request.setHeader("Accept-Encoding", "gzip")
        val response = client.execute(request)

        assertThat(response.statusLine.statusCode).isEqualTo(200)
        response.allHeaders.forEach { h -> println(h) }
        assertThat(response.getFirstHeader("Content-Encoding").value).isEqualTo("gzip")
    }

}