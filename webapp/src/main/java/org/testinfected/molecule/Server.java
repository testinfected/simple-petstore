package org.testinfected.molecule;

import java.io.IOException;

public interface Server {
    int port();

    void run(Application app) throws IOException;

    void shutdown() throws IOException;
}
