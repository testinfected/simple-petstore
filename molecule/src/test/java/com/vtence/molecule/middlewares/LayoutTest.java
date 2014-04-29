package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.lib.TextBody;
import com.vtence.molecule.decoration.ContentProcessor;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.decoration.Selector;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;

public class LayoutTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    Selector selector = context.mock(Selector.class);
    Layout layout = new Layout(selector, new StubProcessor(), new StubDecorator());

    States page = context.states("page").startsAs("selected");

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Before public void
    selectPage() throws Exception {
        context.checking(new Expectations() {{
            allowing(selector).selected(with(any(Response.class))); will(returnValue(true)); when(page.is("selected"));
            allowing(selector).selected(with(any(Response.class))); will(returnValue(false)); when(page.isNot("selected"));
        }});
    }

    @Test public void
    runsContentThroughDecoratorWhenPageIsSelected() throws Exception {
        layout.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("raw content");
            }
        });

        layout.handle(request, response);
        response.assertBody("<decorated>raw content</decorated>");
    }

    @Test public void
    removesContentLengthHeaderIfDecorating() throws Exception {
        response.setLong("Content-Length", 140);
        layout.handle(request, response);
        response.assertHeader("Content-Length", nullValue());
    }

    @Test public void
    leavesContentUntouchedIfNoDecorationOccurs() throws Exception {
        layout.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("original content");
            }
        });
        page.become("unselected");
        layout.handle(request, response);
        response.assertBody("original content");
    }

    @Test public void
    preservesOriginalResponseEncodingWhenDecorating() throws Exception {
        layout.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("encoded content (éçëœ)");
            }
        });

        response.contentType("text/html; charset=utf-8");
        layout.handle(request, response);

        response.assertContentType("text/html; charset=utf-8");
        response.assertContentEncodedAs("utf-8");
        response.assertBody(containsString("éçëœ"));
    }

    private class StubProcessor implements ContentProcessor {
        public Map<String, String> process(String content) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("content", content);
            return data;
        }
    }

    private class StubDecorator implements Decorator {
        public Body merge(Request request, Map<String, String> content) throws IOException {
            return TextBody.text("<decorated>" + content.get("content") + "</decorated>");
        }
    }
}
