package org.testinfected.petstore.dispatch;

public interface Routing {

    void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception;
}
