package org.testinfected.petstore.routing;

import org.simpleframework.http.Request;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.Matcher;

public interface Route extends Matcher<Request>, Application {
}
