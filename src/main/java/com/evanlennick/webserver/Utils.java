package com.evanlennick.webserver;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Utils {

    private Utils() {}

    public static String getRfc1123FormattedDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

}
