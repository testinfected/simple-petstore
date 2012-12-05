package org.testinfected.support.routing;

import org.testinfected.support.Application;
import org.testinfected.support.Matcher;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

public interface Route extends Matcher<Request>, Application {

    public void handle(Request request, Response response) throws Exception;
}
