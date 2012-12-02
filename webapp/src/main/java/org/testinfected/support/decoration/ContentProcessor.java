package org.testinfected.support.decoration;

import java.util.Map;

public interface ContentProcessor {

    Map<String, Object> process(String content);
}
