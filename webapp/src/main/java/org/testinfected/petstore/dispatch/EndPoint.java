package org.testinfected.petstore.dispatch;

public interface EndPoint {

    void process(Dispatch.Request request, Dispatch.Response response) throws Exception;
}
