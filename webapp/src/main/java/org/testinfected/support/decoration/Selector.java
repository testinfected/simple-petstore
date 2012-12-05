package org.testinfected.support.decoration;

import org.testinfected.support.Response;

public interface Selector {

    boolean select(Response response);
}
