package org.testinfected.molecule.decoration;

import org.testinfected.molecule.Response;

public interface Selector {

    boolean select(Response response);
}
