package org.testinfected.petstore.dispatch;

public interface Route {

    boolean matches(Dispatch.Request request);

    void dispatch(Dispatch.Request request, Dispatch.Response response) throws Exception;
}
