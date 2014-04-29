package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.AbstractMiddleware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaticAssets extends AbstractMiddleware {

    private final Application fileServer;
    private final List<String> urls = new ArrayList<String>();

    private String indexFile = "index.html";

    public StaticAssets(Application fileServer, String... urls) {
        this.fileServer = fileServer;
        serve(urls);
    }

    public StaticAssets serve(String... urls) {
        this.urls.addAll(Arrays.asList(urls));
        return this;
    }

    public StaticAssets index(String indexFile) {
        this.indexFile = indexFile;
        return this;
    }

    public void handle(Request request, Response response) throws Exception {
        if (canServe(request.path())) {
            serve(request, response);
        } else {
            forward(request, response);
        }
    }

    private void serve(Request request, Response response) throws Exception {
        if (targetsDirectory(request)) {
            request.path(request.path() + indexFile);
        }
        fileServer.handle(request, response);
    }

    private boolean targetsDirectory(Request request) {
        return request.path().endsWith("/");
    }

    private boolean canServe(String path) throws Exception {
        return routeDefinedFor(path);
    }

    private boolean routeDefinedFor(String path) {
        for (String url : urls) {
            if (path.startsWith(url)) return true;
        }
        return false;
    }
}
