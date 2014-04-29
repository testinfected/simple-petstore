package com.vtence.molecule.middlewares;

import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.lib.Matcher;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.Matchers;
import com.vtence.molecule.lib.Middleware;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.vtence.molecule.lib.Matchers.withPath;

public class FilterMap extends AbstractMiddleware {

    private final Map<Matcher<? super Request>, Middleware> filters = new LinkedHashMap<Matcher<? super Request>, Middleware>();

    public void handle(Request request, Response response) throws Exception {
        Middleware filter = filterMappedTo(request);
        filter.connectTo(successor);
        filter.handle(request, response);
    }

    private Middleware filterMappedTo(Request request) {
        Middleware bestMatch = new PassThrough();
        for (Matcher<? super Request> requestMatcher : filters.keySet()) {
            if (requestMatcher.matches(request)) bestMatch = filters.get(requestMatcher);
        }
        return bestMatch;
    }

    public FilterMap map(String pathPrefix, Middleware filter) {
        return map(withPath(Matchers.startingWith(pathPrefix)), filter);
    }

    public FilterMap map(Matcher<? super Request> requestMatcher, Middleware filter) {
        filters.put(requestMatcher, filter);
        return this;
    }

    private static class PassThrough extends AbstractMiddleware {
        public void handle(Request request, Response response) throws Exception {
            successor.handle(request, response);
        }
    }
}
