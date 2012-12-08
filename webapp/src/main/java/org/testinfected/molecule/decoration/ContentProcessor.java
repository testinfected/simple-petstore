package org.testinfected.molecule.decoration;

import java.util.Map;

public interface ContentProcessor {

    Map<String, Object> process(String content);
}
