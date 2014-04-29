package org.testinfected.petstore;

import com.vtence.molecule.Response;

import java.io.IOException;

public interface View<T> {

    void render(Response response, T context) throws IOException;
}
