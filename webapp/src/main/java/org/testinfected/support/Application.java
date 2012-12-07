package org.testinfected.support;

public interface Application {

    void handle(Request request, Response response) throws Exception;

    void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception;
}
