package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.decoration.Decorator;
import org.testinfected.molecule.decoration.Selector;
import org.testinfected.molecule.middlewares.SiteMesh;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import java.io.IOException;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;
import static test.support.org.testinfected.molecule.web.WriteBody.writeBody;

@RunWith(JMock.class)
public class SiteMeshTest {
    Mockery context = new JUnit4Mockery();
    Selector selector = context.mock(Selector.class);
    Application successor = context.mock(Application.class, "successor");
    SiteMesh siteMesh = new SiteMesh(selector, new FakeDecorator());

    String originalPage = "<plain page>";
    String decoratedPage = "<decorated page>";
    States page = context.states("page").startsAs("selected");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor() throws Exception {
        context.checking(new Expectations() {{
            allowing(selector).select(with(any(Response.class))); will(returnValue(true)); when(page.is("selected"));
            allowing(selector).select(with(any(Response.class))); will(returnValue(false)); when(page.isNot("selected"));
            allowing(successor).handle(with(request), with(any(Response.class))); will(writeBody(originalPage));
        }});

        siteMesh.connectTo(successor);
    }

    @Test public void
    runsContentThroughDecoratorWhenPageIsSelected() throws Exception {
        siteMesh.handle(request, response);

        response.assertBody(decoratedPage);
    }


    @Test public void
    removesContentLengthHeaderWhenPageIsSelected() throws Exception {
        response.header("Content-Length", String.valueOf(140));
        siteMesh.handle(request, response);

        response.assertHeader("Content-Length", nullValue());
    }

    @Test public void
    doesNotDecorateContentWhenPageIsNotSelected() throws Exception {
        page.become("unselected");

        siteMesh.handle(request, response);

        response.assertBody(originalPage);
    }

    @Test public void
    preservesOriginalPageEncodingWhenDecorating() throws Exception {
        response.withContentType("text/html; charset=UTF-16");
        decoratedPage = "<The following characters require encoding: éçë>";

        siteMesh.handle(request, response);

        response.assertContentType(containsString("UTF-16"));
        response.assertContentEncodedAs("UTF-16");
    }

    private class FakeDecorator implements Decorator {
        public void decorate(Writer out, String content) throws IOException {
            out.write(decoratedPage);
        }
    }
}
