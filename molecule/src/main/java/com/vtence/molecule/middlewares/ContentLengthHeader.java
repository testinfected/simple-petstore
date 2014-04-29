package com.vtence.molecule.middlewares;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;

import static com.vtence.molecule.http.HeaderNames.CONTENT_LENGTH;
import static com.vtence.molecule.http.HeaderNames.TRANSFER_ENCODING;

public class ContentLengthHeader extends AbstractMiddleware {

    public void handle(Request request, Response response) throws Exception {
        forward(request, response);
        if (!hasContentLengthHeader(response)
                && isFixedLengthSize(response)
                && !isChunked(response)) {
            response.contentLength(response.size());
        }
    }

    private boolean isFixedLengthSize(Response response) {
        return response.size() > 0;
    }

    private boolean hasContentLengthHeader(Response response) {
        return response.has(CONTENT_LENGTH);
    }

    private boolean isChunked(Response response) {
        return response.has(TRANSFER_ENCODING)
                && response.get(TRANSFER_ENCODING).equalsIgnoreCase("chunked");
    }
}
