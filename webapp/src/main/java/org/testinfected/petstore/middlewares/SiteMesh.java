package org.testinfected.petstore.middlewares;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.decoration.Decorator;
import org.testinfected.petstore.decoration.Selector;
import org.testinfected.petstore.util.BufferedResponse;
import org.testinfected.petstore.util.Matcher;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.testinfected.petstore.util.Matchers.hasNormalizedPath;
import static org.testinfected.petstore.util.StartingWith.startingWith;

public class SiteMesh extends AbstractMiddleware {

    private final Selector selector;
    private final List<Decoration> decorations = new ArrayList<Decoration>();

    public SiteMesh(Selector selector) {
        this.selector = selector;
    }

    public void map(String path, Decorator decorator) {
        map(hasNormalizedPath(startingWith(path)), decorator);
    }

    public void map(Matcher<Request> matcher, Decorator decorator) {
        decorations.add(new Decoration(matcher, decorator));
    }

    public void handle(Request request, Response response) throws Exception {
        String body = captureResponse(request, response);
        if (subjectToDecoration(request, response)) {
            decorate(decoratorFor(request), response, body);
        } else {
            write(response, body);
        }
    }

    private void decorate(Decorator decorator, Response response, String content) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
        decorator.decorate(out, content);
        out.flush();
    }

    private void write(Response response, String body) throws IOException {
        response.getPrintStream().print(body);
    }

    private String captureResponse(Request request, Response response) throws Exception {
        BufferedResponse buffer = new BufferedResponse(response);
        forward(request, buffer);
        return buffer.getBody();
    }

    private boolean subjectToDecoration(Request request, Response response) {
        return isMapped(request) && isSelected(response);
    }

    private boolean isMapped(Request request) {
        return decoratorFor(request) != null;
    }

    private boolean isSelected(Response response) {
        return selector.select(response);
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

        private final Matcher<Request> matcher;
        private final Decorator decorator;

        public Decoration(Matcher<Request> matcher, Decorator decorator) {
            this.matcher = matcher;
            this.decorator = decorator;
        }

        private boolean appliesTo(Request request) {
            return matcher.matches(request);
        }
    }
}
