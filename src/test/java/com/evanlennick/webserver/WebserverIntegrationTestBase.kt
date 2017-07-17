package com.evanlennick.webserver

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(WebserverTestRunner::class)
open class WebserverIntegrationTestBase {

    lateinit var baseUrl: String

    lateinit var client: HttpClient

    @Before fun setup() {
        client = HttpClientBuilder.create().build()
        baseUrl = "http://localhost:3353/"
    }
}