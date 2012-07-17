package org.testinfected.petstore.util;

import org.simpleframework.http.Request;

public interface RequestMatcher {

    boolean matches(Request request);
}
