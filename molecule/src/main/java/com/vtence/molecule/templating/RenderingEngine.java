package com.vtence.molecule.templating;

import java.io.IOException;
import java.io.Writer;

public interface RenderingEngine {

    void render(Writer out, String view, Object context) throws IOException;
}
