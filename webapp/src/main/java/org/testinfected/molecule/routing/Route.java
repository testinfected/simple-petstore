package org.testinfected.molecule.routing;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.util.Matcher;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

public interface Route extends Matcher<Request>, Application {

    public void handle(Request request, Response response) throws Exception;
}
