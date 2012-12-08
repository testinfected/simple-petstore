package org.testinfected.molecule;

public interface Middleware extends Application {

    void connectTo(Application successor);
}
