package com.vtence.molecule.routing;

import java.util.Map;

public interface WithBoundParameters {

    Map<String, String> boundParameters(String path);
}
