package com.evanlennick.webserver;

import static java.lang.System.getProperty;

public class Constants {
    public static final String SYSTEM_EOL = getProperty("line.separator");
    public static final String HTTP_EOL = "\r\n";
    public static final String SERVER_STRING = "elennick-webserver";
}
