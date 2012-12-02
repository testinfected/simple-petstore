package org.testinfected.support.routing;

import org.simpleframework.http.Request;
import org.testinfected.support.Application;
import org.testinfected.support.Matcher;

public interface Route extends Matcher<Request>, Application {
}
