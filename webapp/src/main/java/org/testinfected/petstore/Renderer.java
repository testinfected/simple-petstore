package org.testinfected.petstore;

import java.io.IOException;

// todo Rename to RenderingEngine
public interface Renderer {

    String render(String template, Object context) throws IOException;
}
