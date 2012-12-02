package org.testinfected.support;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface Application {

    void handle(Request request, Response response) throws Exception;
}
