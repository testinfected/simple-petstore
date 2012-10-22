package org.testinfected.petstore.util;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;

public class RequestHasPath implements Matcher<Request> {

    private Matcher<Path> path;

    public RequestHasPath(Matcher<Path> path) {
        this.path = path;
    }

    public boolean matches(Request actual) {
        return path.matches(actual.getPath());
    }

    public static RequestHasPath hasPath(Matcher<Path> path) {
        return new RequestHasPath(path);
    }

    public static RequestHasPath hasNormalizedPath(final Matcher<? super String> path) {
        return new RequestHasPath(new Matcher<Path>() {
            public boolean matches(Path actual) {
                return path.matches(actual.getPath());
            }
        });
    }
}
