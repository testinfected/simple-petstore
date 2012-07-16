package org.testinfected.petstore.decoration;

import java.util.Map;

public interface ContentProcessor {

    Map<String, String> process(String content);
}
