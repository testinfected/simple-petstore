package org.testinfected.petstore.routing;

import org.testinfected.petstore.Application;
import org.testinfected.petstore.util.RequestMatcher;

public interface Route extends RequestMatcher, Application {
}
