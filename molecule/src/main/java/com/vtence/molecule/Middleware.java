package com.vtence.molecule;

public interface Middleware extends Application {

    void connectTo(Application successor);
}
