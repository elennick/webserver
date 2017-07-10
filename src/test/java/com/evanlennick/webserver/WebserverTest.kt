package com.evanlennick.webserver

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethodBase
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.*

class WebserverTest {
    lateinit var client: HttpClient

    lateinit var method: HttpMethodBase

    companion object {
        @JvmStatic
        @BeforeClass fun startServer() {
            Main.main(arrayOf("3353"))
        }

        @JvmStatic
        @AfterClass fun stopServer() {
            Main.stop()
        }
    }

    @Before fun setup() {
        client = HttpClient()
    }

    @After fun teardown() {
        method.releaseConnection()
    }

    @Ignore //TODO temporarily disable this until i set something up to have a test file available to serve
    @Test fun test200Ok() {
        method = GetMethod("http://localhost:3353/index.html")
        val statusCode = client.executeMethod(method)

        assertThat(statusCode).isEqualTo(200)
    }

    @Test fun test404NotFound() {
        method = GetMethod("http://localhost:3353/doesntexist.html")
        val statusCode = client.executeMethod(method)

        assertThat(statusCode).isEqualTo(404)
    }

    @Test fun test501NotImplemented() {
        method = PostMethod("http://localhost:3353/whatever.html")
        val statusCode = client.executeMethod(method)

        assertThat(statusCode).isEqualTo(501)
    }

}