package test.org.testinfected.petstore.templates;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class HeaderTest {

    Element content;

    @Before public void
    renderContent() {
        content = render("layout/header").from(WebRoot.locate()).asDom();
    }

    @Test public void
    logoLinksToHome() {
        assertThat("content", content, hasUniqueSelector("#logo a", hasAttribute("href", "/")));
    }
}
