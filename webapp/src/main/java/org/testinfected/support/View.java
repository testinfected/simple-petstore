package org.testinfected.support;

import java.io.IOException;
import java.io.Writer;

public interface View<T> {

    void render(Writer out, T context) throws IOException;
}
