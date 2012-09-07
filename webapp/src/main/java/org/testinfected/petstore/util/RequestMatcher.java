package org.testinfected.petstore.util;

import org.simpleframework.http.Request;

// todo generify to have matchers on path as well
public interface RequestMatcher {

    boolean matches(Request request);
}
