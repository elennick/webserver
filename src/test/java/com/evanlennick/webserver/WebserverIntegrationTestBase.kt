package com.evanlennick.webserver

import org.junit.runner.RunWith
import java.net.HttpURLConnection
import java.net.URL

@RunWith(WebserverTestRunner::class)
open class WebserverIntegrationTestBase {
    protected val baseUrl = "http://localhost:3353/"
    protected val testHtmlUrl = URL(baseUrl + "test.html")
    protected val con = testHtmlUrl.openConnection() as HttpURLConnection
}