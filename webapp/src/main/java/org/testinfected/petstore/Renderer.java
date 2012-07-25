package org.testinfected.petstore;

import java.io.IOException;

public interface Renderer {

    String render(String template, Object context) throws IOException;
}
