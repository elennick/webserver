package com.evanlennick.webserver.integrationtests

import com.evanlennick.webserver.WebserverIntegrationTestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ResponseCodesTest : WebserverIntegrationTestBase() {

    @Test fun test200Ok() {
        val request = HttpGet(baseUrl + "test.html")
        val response = client.execute(request)

        assertThat(response.statusLine.statusCode).isEqualTo(200)
    }

    @Test fun test404NotFound() {
        val request = HttpGet(baseUrl + "doesntexist.html")
        val response = client.execute(request)

        assertThat(response.statusLine.statusCode).isEqualTo(404)
    }

    @Test fun test501NotImplemented() {
        val request = HttpPost(baseUrl + "post.html")
        val response = client.execute(request)

        assertThat(response.statusLine.statusCode).isEqualTo(501)
    }

}