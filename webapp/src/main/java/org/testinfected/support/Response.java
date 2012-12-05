package org.testinfected.support;

import java.io.IOException;

public interface Response {

    void render(String view, Object context) throws IOException;

    void redirectTo(String location);

    void renderHead(int statusCode);

    int statusCode();

    int contentLength();

    <T> T unwrap(Class<T> type);
}
