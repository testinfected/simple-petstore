package org.testinfected.petstore;

import java.io.Writer;

public interface RenderingEngine {

    void render(Writer out, String view, Object context);
}
