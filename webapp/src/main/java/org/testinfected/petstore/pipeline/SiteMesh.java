package org.testinfected.petstore.pipeline;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.decoration.Decorator;
import org.testinfected.petstore.decoration.Selector;
import org.testinfected.petstore.util.BufferedResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SiteMesh extends AbstractMiddleware {

    private final Selector selector;
    private final List<Decoration> decorations = new ArrayList<Decoration>();

    public SiteMesh(Selector selector) {
        this.selector = selector;
    }

    public void decorate(String path, Decorator decorator) {
        decorations.add(new Decoration(path, decorator));
    }

    public void handle(Request request, Response response) throws Exception {
        BufferedResponse buffer = new BufferedResponse(response);
        forward(request, buffer);
        if (shouldDecorate(request, buffer)) decorate(request, buffer);
        buffer.flush();
    }

    private boolean shouldDecorate(Request request, Response response) {
        return underDecoration(request) && selected(response);
    }

    private boolean underDecoration(Request request) {
        return decoratorFor(request) != null;
    }

    private boolean selected(Response response) {
        return selector.select(response);
    }

    private void decorate(Request request, BufferedResponse response) throws IOException {
        String decorated = decoratorFor(request).decorate(response.getBody());
        response.reset();
        response.getPrintStream().print(decorated);
    }

    private Decorator decoratorFor(Request request) {
        ListIterator<Decoration> iterator = decorations.listIterator(decorations.size());
        while (iterator.hasPrevious()) {
            Decoration decoration = iterator.previous();
            if (decoration.appliesTo(request)) return decoration.decorator;
        }
        return null;
    }

    private static class Decoration {

        private final String path;
        private final Decorator decorator;

        private Decoration(String path, Decorator decorator) {
            this.path = path;
            this.decorator = decorator;
        }

        private boolean appliesTo(Request request) {
            return pathOf(request).startsWith(path);
        }

        private String pathOf(Request request) {
            return request.getPath().getPath();
        }
    }

}
