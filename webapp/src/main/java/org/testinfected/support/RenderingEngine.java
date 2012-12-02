package org.testinfected.support;

import java.io.Writer;

public interface RenderingEngine {

    void render(Writer out, String view, Object context);
}
