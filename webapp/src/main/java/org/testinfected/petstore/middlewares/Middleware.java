package org.testinfected.petstore.middlewares;

import org.testinfected.petstore.Application;

public interface Middleware extends Application {

    void chain(Application app);
}
