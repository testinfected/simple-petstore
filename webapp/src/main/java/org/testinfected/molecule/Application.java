package org.testinfected.molecule;

public interface Application {

    void handle(Request request, Response response) throws Exception;
}
