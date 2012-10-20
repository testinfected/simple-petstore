package org.testinfected.petstore;

import java.io.IOException;

public interface Controller {

    void process(Request request, Response response) throws Exception;

    interface Request {

        String getParameter(String name);
    }

    interface Response {

        void render(String view, Object context) throws IOException;

        void redirectTo(String location);

        void renderHead(int statusCode);
    }
}
