package org.testinfected.petstore;

import org.testinfected.support.Request;
import org.testinfected.support.Response;

public interface Controller {

    void handle(Request request, Response response) throws Exception;
}
