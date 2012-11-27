package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.util.Matcher;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testinfected.petstore.util.Matchers.hasNormalizedPath;
import static org.testinfected.petstore.util.Matchers.startingWith;

public class FilterMap extends AbstractMiddleware {

    private final Map<Matcher<Request>, Middleware> filters = new LinkedHashMap<Matcher<Request>, Middleware>();

    @Override
    public void handle(Request request, Response response) throws Exception {
        Middleware filter = filterMappedTo(request);
        filter.connectTo(successor);
        filter.handle(request, response);
    }

    private Middleware filterMappedTo(Request request) {
        Middleware bestMatch = new PassThrough();
        for (Matcher<Request> requestMatcher : filters.keySet()) {
            if (requestMatcher.matches(request)) bestMatch = filters.get(requestMatcher);
        }
        return bestMatch;
    }

    public void map(String pathPrefix, Middleware filter) {
        filters.put(hasNormalizedPath(startingWith(pathPrefix)), filter);
    }

    public void map(Matcher<Request> requestMatcher, Middleware filter) {
        filters.put(requestMatcher, filter);
    }

    private static class PassThrough extends AbstractMiddleware {
        public void handle(Request request, Response response) throws Exception {
            forward(request, response);
        }
    }
}
