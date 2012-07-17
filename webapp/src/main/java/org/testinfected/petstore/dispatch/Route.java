package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;

public interface Route {

    boolean handles(Request request);
}
