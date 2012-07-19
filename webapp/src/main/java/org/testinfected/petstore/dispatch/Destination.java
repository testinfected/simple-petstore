package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;

public interface Destination {

    void handle(Request request, Response response, Dispatcher dispatcher) throws IOException;
}
