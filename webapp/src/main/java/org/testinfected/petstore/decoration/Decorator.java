package org.testinfected.petstore.decoration;

import java.io.IOException;

public interface Decorator {

    String decorate(String content) throws IOException;
}
