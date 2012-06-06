package org.testinfected.petstore;

import org.simpleframework.http.Response;

import java.io.IOException;

public interface Renderer {

    void render(String name, Object context, Response response) throws IOException;
}
