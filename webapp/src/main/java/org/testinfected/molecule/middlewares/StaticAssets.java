package org.testinfected.molecule.middlewares;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticAssets extends AbstractMiddleware {

    private final Application fileServer;
    private final List<String> urls = new ArrayList<String>();

    public StaticAssets(Application fileServer, String... urls) {
        this.fileServer = fileServer;
        this.urls.addAll(Arrays.asList(urls));
    }

    public void serve(String... urls) {
        this.urls.addAll(Arrays.asList(urls));
    }

    public void handle(Request request, Response response) throws Exception {
        if (assetRequested(request.pathInfo())) {
            serve(request, response);
        } else {
            forward(request, response);
        }
    }

    private void serve(Request request, Response response) throws Exception {
        fileServer.handle(request, response);
    }

    private boolean assetRequested(String path) throws Exception {
        for (String url : urls) {
            if (path.startsWith(url)) return true;
        }
        return false;
    }
}
