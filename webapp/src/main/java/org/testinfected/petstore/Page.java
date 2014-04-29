package org.testinfected.petstore;

import com.vtence.molecule.Response;

import java.io.IOException;

public interface Page {

    void render(Response response, Object context) throws IOException;
}
