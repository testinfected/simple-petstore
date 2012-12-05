package org.testinfected.support.middlewares;

import org.testinfected.support.*;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.testinfected.support.matchers.Matchers.startingWith;
import static org.testinfected.support.matchers.Matchers.withPath;

public class FilterMap extends AbstractMiddleware {

    private final Map<Matcher<Request>, Middleware> filters = new LinkedHashMap<Matcher<Request>, Middleware>();

    public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
        handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
    }

    public void handle(Request request, Response response) throws Exception {
        Middleware filter = filterMappedTo(request);
        filter.connectTo(successor);
        filter.handle(request.unwrap(org.simpleframework.http.Request.class), response.unwrap(org.simpleframework.http.Response.class));
    }

    private Middleware filterMappedTo(Request request) {
        Middleware bestMatch = new PassThrough();
        for (Matcher<Request> requestMatcher : filters.keySet()) {
            if (requestMatcher.matches(request)) bestMatch = filters.get(requestMatcher);
        }
        return bestMatch;
    }

    public void map(String pathPrefix, Middleware filter) {
        filters.put(withPath(startingWith(pathPrefix)), filter);
    }

    public void map(Matcher<Request> requestMatcher, Middleware filter) {
        filters.put(requestMatcher, filter);
    }

    private static class PassThrough extends AbstractMiddleware {
        public void handle(Request request, Response response) throws Exception {
            forward(request, response);
        }

        public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
            forward(request, response);
        }
    }
}
