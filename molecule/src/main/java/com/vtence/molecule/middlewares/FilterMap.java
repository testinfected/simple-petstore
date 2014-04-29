package com.vtence.molecule.middlewares;

import com.vtence.molecule.util.Matcher;
import com.vtence.molecule.Middleware;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.vtence.molecule.matchers.Matchers.startingWith;
import static com.vtence.molecule.matchers.Matchers.withPath;

public class FilterMap extends AbstractMiddleware {

    private final Map<Matcher<Request>, Middleware> filters = new LinkedHashMap<Matcher<Request>, Middleware>();

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
        filters.put(withPath(startingWith(pathPrefix)), filter);
    }

    public void map(Matcher<Request> requestMatcher, Middleware filter) {
        filters.put(requestMatcher, filter);
    }

    private static class PassThrough extends AbstractMiddleware {
        public void handle(Request request, Response response) throws Exception {
            successor.handle(request, response);
        }
    }
}
