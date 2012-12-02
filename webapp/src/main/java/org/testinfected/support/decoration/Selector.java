package org.testinfected.support.decoration;

import org.simpleframework.http.Response;

public interface Selector {

    boolean select(Response response);
}
