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
            Main.isTestMode = true
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

    @Test fun test200Ok() {
        method = GetMethod("http://localhost:3353/test.html")
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