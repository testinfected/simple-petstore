package org.testinfected.molecule.matchers;

import org.testinfected.molecule.util.Matcher;
import org.testinfected.molecule.Request;

public class RequestWithPath implements Matcher<Request> {

    private Matcher<? super String> path;

    public RequestWithPath(Matcher<? super String> pathMatcher) {
        this.path = pathMatcher;
    }

    public boolean matches(Request actual) {
        return path.matches(actual.pathInfo());
    }

    public static RequestWithPath withPath(Matcher<? super String> pathMatcher) {
        return new RequestWithPath(pathMatcher);
    }
}
