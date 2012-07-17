package org.testinfected.petstore.decoration;

import org.simpleframework.http.Request;
import org.testinfected.petstore.util.RequestMatcher;

public class PathMapper {

    public PathMapper() {}

    public static RequestMatcher startingWith(final String prefix) {
        return new RequestMatcher() {
            public boolean matches(Request request) {
                return pathOf(request).startsWith(prefix);
            }

            private String pathOf(Request request) {
                return request.getPath().getPath();
            }
        };
    }
}
