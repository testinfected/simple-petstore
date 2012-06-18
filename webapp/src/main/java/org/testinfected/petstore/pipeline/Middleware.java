package org.testinfected.petstore.pipeline;

import org.testinfected.petstore.Application;

public interface Middleware extends Application {

    void chain(Application app);
}
