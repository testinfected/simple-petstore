package org.testinfected.petstore.dispatch;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;

public interface Route {

    boolean matches(Dispatch.Request request);

    void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception;

    boolean matches(Request request);

    void dispatch(Request request, Response response, Dispatcher dispatcher) throws IOException;
}
