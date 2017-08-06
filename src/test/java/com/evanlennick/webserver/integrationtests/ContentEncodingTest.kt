package com.evanlennick.webserver.integrationtests

import com.evanlennick.webserver.WebserverIntegrationTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ContentEncodingTest : WebserverIntegrationTestBase() {

    private val EXPECTED_LENGTH_OF_TESTHTML_WITH_NO_ENCODING = "1205"

    private val EXPECTED_LENGTH_OF_TESTHTML_GZIPPED = "94"

    @Before fun setup() {
        con.requestMethod = "GET"
    }

    @Test fun testNoEncoding() {
        con.setRequestProperty("Accept-Encoding", "none")

        val code = con.responseCode
        val contentEncoding = con.headerFields["Content-Encoding"]?.get(0)
        val contentLength = con.headerFields["Content-Length"]?.get(0)

        assertThat(code).isEqualTo(200)
        assertThat(contentEncoding).isNull()
        assertThat(contentLength).isEqualTo(EXPECTED_LENGTH_OF_TESTHTML_WITH_NO_ENCODING)
    }

    @Test fun testGzipEncoding() {
        con.setRequestProperty("Accept-Encoding", "gzip")

        val code = con.responseCode
        val contentEncoding: String? = con.headerFields["Content-Encoding"]?.get(0)
        val contentLength = con.headerFields["Content-Length"]?.get(0)

        assertThat(code).isEqualTo(200)
        assertThat(contentEncoding).isEqualTo("gzip")
        assertThat(contentLength).isEqualTo(EXPECTED_LENGTH_OF_TESTHTML_GZIPPED)
    }

}