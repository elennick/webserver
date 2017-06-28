package com.evanlennick.webserver

import java.lang.System.getProperty

object Constants {
    val SYSTEM_EOL = getProperty("line.separator")
    val HTTP_EOL = "\r\n"
    val SERVER_STRING = "elennick-webserver"
}
