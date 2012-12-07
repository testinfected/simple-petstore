package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;

public class ContentType extends AbstractMiddleware {

    private final String fallbackContentType;

    public ContentType(String fallbackContentType) {
        this.fallbackContentType = fallbackContentType;
    }

    public void handle(Request request, Response response) throws Exception {
        if (response.contentType() == null) response.contentType(fallbackContentType);
    }
}
