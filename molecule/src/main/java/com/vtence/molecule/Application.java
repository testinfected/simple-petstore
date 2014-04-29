package com.vtence.molecule;

public interface Application {

    void handle(Request request, Response response) throws Exception;
}
