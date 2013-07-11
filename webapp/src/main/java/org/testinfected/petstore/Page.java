package org.testinfected.petstore;

import org.testinfected.molecule.Response;
import org.testinfected.petstore.util.Context;

import java.io.IOException;

public interface Page {

    void render(Response response, Context context) throws IOException;
}
