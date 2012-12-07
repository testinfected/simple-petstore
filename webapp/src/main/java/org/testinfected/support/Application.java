package org.testinfected.support;

public interface Application {

    void handle(Request request, Response response) throws Exception;
}
