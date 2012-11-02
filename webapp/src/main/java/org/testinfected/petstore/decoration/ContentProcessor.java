package org.testinfected.petstore.decoration;

import java.util.Map;

public interface ContentProcessor {

    Map<String, Object> process(String content);
}
