package com.evanlennick.webserver

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class WebserverTest {
    lateinit var client: HttpClient

    companion object {
        @JvmStatic
        @BeforeClass fun startServer() {
            Main.main(arrayOf("80"))
        }

        @JvmStatic
        @AfterClass fun teardown() {
            Main.stop()
        }
    }

    @Before fun setup() {
        client = HttpClient()
    }

    @Test fun test200Ok() {
        val method = GetMethod("http://localhost/index.html")
        val statusCode = client.executeMethod(method)
        method.releaseConnection()

        assertThat(statusCode).isEqualTo(200)
    }

    @Test fun test404NotFound() {
        val method = GetMethod("http://localhost/doesntexist.html")
        val statusCode = client.executeMethod(method)
        method.releaseConnection()

        assertThat(statusCode).isEqualTo(404)
    }

    @Test fun test501NotImplemented() {
        val method = PostMethod("http://localhost/whatever.html")
        val statusCode = client.executeMethod(method)
        method.releaseConnection()

        assertThat(statusCode).isEqualTo(501)
    }

}