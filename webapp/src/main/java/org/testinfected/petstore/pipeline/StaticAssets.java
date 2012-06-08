package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticAssets extends AbstractMiddleware {

    private final Handler fileServer;
    private final List<String> urls = new ArrayList<String>();

    public StaticAssets(Handler fileServer, String... urls) {
        this.fileServer = fileServer;
        this.urls.addAll(Arrays.asList(urls));
    }

    public void serve(String... urls) {
        this.urls.addAll(Arrays.asList(urls));
    }

    public void handle(Request request, Response response) throws Exception {
        if (assetRequested(request.getPath())) {
            serve(request, response);
        } else {
            forward(request, response);
        }
    }

    private void serve(Request request, Response response) throws Exception {
        fileServer.handle(request, response);
    }

    private boolean assetRequested(Path request) throws Exception {
        for (String url : urls) {
            if (request.getPath().startsWith(url)) return true;
        }
        return false;
    }
}
