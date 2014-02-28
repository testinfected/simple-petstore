package org.testinfected.molecule.routing;

import java.util.Map;

public interface WithBoundParameters {

    Map<String, String> boundParameters(String path);
}
