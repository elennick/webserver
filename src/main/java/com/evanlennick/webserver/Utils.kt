package com.evanlennick.webserver

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Utils {

    val rfc1123FormattedDateTime: String
        get() {
            val now = ZonedDateTime.now(ZoneId.of("GMT"))
            return DateTimeFormatter.RFC_1123_DATE_TIME.format(now)
        }

}
