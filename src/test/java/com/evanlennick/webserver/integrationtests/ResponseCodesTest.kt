package com.evanlennick.webserver.integrationtests

import com.evanlennick.webserver.WebserverIntegrationTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.net.HttpURLConnection
import java.net.URL

class ResponseCodesTest : WebserverIntegrationTestBase() {

    @Test fun test200Ok() {
        con.requestMethod = "GET"
        assertThat(con.responseCode).isEqualTo(200)
    }

    @Test fun test404NotFound() {
        val doesntExistHtmlUrl = URL(baseUrl + "doesntexist.html")
        val doesntExistCon = doesntExistHtmlUrl.openConnection() as HttpURLConnection
        doesntExistCon.requestMethod = "GET"

        assertThat(doesntExistCon.responseCode).isEqualTo(404)
    }

    @Test fun test501NotImplemented() {
        con.requestMethod = "POST"
        assertThat(con.responseCode).isEqualTo(501)
    }

}