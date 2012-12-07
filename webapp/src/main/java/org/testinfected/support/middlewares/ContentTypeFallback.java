package org.testinfected.support.middlewares;

import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.ResponseWrapper;

public class ContentTypeFallback extends AbstractMiddleware {

    private final String fallbackContentType;

    public ContentTypeFallback(String fallbackContentType) {
        this.fallbackContentType = fallbackContentType;
    }

    public void handle(Request request, Response response) throws Exception {
        forward(request, new ResponseWrapper(response) {
            public String contentType() {
                return super.contentType() != null ? super.contentType() : fallbackContentType;
            }
        });

        if (response.contentType() == null) response.contentType(fallbackContentType);
    }
}
