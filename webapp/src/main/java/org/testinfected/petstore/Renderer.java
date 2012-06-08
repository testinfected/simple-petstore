package org.testinfected.petstore;

import java.io.IOException;

public interface Renderer {

    String render(String name, Object context) throws IOException;
}
