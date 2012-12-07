package org.testinfected.petstore;

import org.testinfected.support.Response;

import java.io.IOException;

public interface Page {

    void render(Response response, Object context) throws IOException;
}
