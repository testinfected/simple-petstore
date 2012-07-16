package org.testinfected.petstore.decoration;

import org.simpleframework.http.Request;

public interface RequestMatcher {

    boolean matches(Request request);
}
