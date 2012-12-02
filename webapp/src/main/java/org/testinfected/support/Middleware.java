package org.testinfected.support;

public interface Middleware extends Application {

    void connectTo(Application successor);
}
