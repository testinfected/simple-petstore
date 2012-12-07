package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.controllers.StaticPage;
import test.support.org.testinfected.support.web.MockRequest;
import test.support.org.testinfected.support.web.MockResponse;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;

@RunWith(JMock.class)
public class StaticPageTest {

    Mockery context = new JUnit4Mockery();
    Page page = context.mock(Page.class);
    StaticPage staticPage = new StaticPage(page);

    MockRequest request = MockRequest.aRequest();
    MockResponse response = MockResponse.aResponse();

    @Test public void
    rendersPageWithEmptyContext() throws Exception {
        context.checking(new Expectations() {{
            oneOf(page).render(with(response), with(equalTo(Collections.emptyMap())));
        }});

        staticPage.handle(request, response);
    }
}

