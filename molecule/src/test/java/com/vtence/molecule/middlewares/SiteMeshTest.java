package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.decoration.Selector;
import com.vtence.molecule.util.Charsets;

import java.io.IOException;
import java.io.Writer;

import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

public class SiteMeshTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    Selector selector = context.mock(Selector.class);
    SiteMesh siteMesh = new SiteMesh(selector, new FakeDecorator());

    String originalPage = "<plain page>";
    String decoratedPage = "<decorated page>";
    States page = context.states("page").startsAs("selected");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    selectPage() throws Exception {
        context.checking(new Expectations() {{
            allowing(selector).select(with(any(Response.class))); will(returnValue(true)); when(page.is("selected"));
            allowing(selector).select(with(any(Response.class))); will(returnValue(false)); when(page.isNot("selected"));
        }});
    }

    @Before public void
    stubApplication() {
        siteMesh.connectTo(write(originalPage));
    }

    @Test public void
    runsContentThroughDecoratorWhenPageIsSelected() throws Exception {
        siteMesh.handle(request, response);
        response.assertBody(decoratedPage);
    }

    @Test public void
    removesContentLengthHeaderWhenDecorating() throws Exception {
        response.header("Content-Length", String.valueOf(140));
        siteMesh.handle(request, response);
        response.assertHeader("Content-Length", nullValue());
    }

    @Test public void
    leavesContentUntouchedWhenNotDecorating() throws Exception {
        page.become("unselected");
        siteMesh.handle(request, response);
        response.assertBody(originalPage);
        int contentSize = originalPage.getBytes(Charsets.UTF_8).length;
        response.assertBufferSize(equalTo(contentSize));
    }

    @Test public void
    preservesOriginalResponseEncodingWhenDecorating() throws Exception {
        response.withContentType("text/html; charset=utf-8");
        decoratedPage = "<The following characters require encoding: éçë>";

        siteMesh.handle(request, response);

        response.assertContentType(containsString("utf-8"));
        response.assertContentEncodedAs("utf-8");
    }

    private Application write(final String text) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(text);
            }
        };
    }

    private class FakeDecorator implements Decorator {
        public void decorate(Writer out, String content) throws IOException {
            out.write(decoratedPage);
        }
    }
}
