package org.testinfected.petstore.dispatch;

import org.testinfected.petstore.util.HttpMethod;

public interface RouteDefinition {
    
    void setPath(String path);

    void setMethod(HttpMethod method);

    void setDestination(Destination destination);
    
    Route toRoute();
}
