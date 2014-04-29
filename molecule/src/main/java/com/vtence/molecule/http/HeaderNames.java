package com.vtence.molecule.http;

// todo group into request and response headers
// todo provide a more complete list with descriptions (see wikipedia)

// todo consistently use across codebase
public interface HeaderNames {
    static final String ACCEPT_ENCODING = "Accept-Encoding";
    static final String ALLOW = "Allow";
    static final String CONTENT_ENCODING = "Content-Encoding";
    static final String CONTENT_LENGTH = "Content-Length";
    static final String CONTENT_TYPE = "Content-Type";
    static final String DATE = "Date";
    static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    static final String LAST_MODIFIED = "Last-Modified";
    static final String LOCATION = "Location";
    static final String SERVER = "Server";
    static final String TRANSFER_ENCODING = "Transfer-Encoding";
}
