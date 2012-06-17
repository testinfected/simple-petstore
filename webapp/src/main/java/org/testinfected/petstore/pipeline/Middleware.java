package org.testinfected.petstore.pipeline;

import org.testinfected.petstore.Handler;

public interface Middleware extends Handler {

    void chain(Handler handler);
}
