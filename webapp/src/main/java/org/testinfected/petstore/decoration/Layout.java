package org.testinfected.petstore.decoration;

import java.io.IOException;
import java.util.Map;

public interface Layout {

    String render(Map<String, String> content) throws IOException;
}
