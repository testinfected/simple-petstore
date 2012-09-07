package org.testinfected.petstore.dispatch;

import java.io.IOException;

public interface Dispatch {

    public interface Request {
        String getParameter(String name);
    }

    public interface Response {
        void render(String template, Object context) throws IOException;

        void redirectTo(String location);
    }
}
