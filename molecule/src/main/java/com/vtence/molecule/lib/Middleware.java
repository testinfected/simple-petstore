package com.vtence.molecule.lib;

import com.vtence.molecule.Application;

public interface Middleware extends Application {

    void connectTo(Application successor);
}
