package org.testinfected.petstore.decoration;

import org.simpleframework.http.Response;

public interface Selector {

    boolean select(Response response);
}
