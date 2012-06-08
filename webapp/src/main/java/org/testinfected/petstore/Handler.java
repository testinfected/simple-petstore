package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface Handler {

    void handle(Request request, Response response) throws Exception;
}
