package org.testinfected.petstore;

import java.io.IOException;

public interface RenderingEngine {

    String render(String template, Object context) throws IOException;
}
