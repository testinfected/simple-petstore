package test.org.testinfected.petstore.templates;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.org.testinfected.petstore.templating.Renderer.render;

public class HeaderTest {

    Element content;

    @Before public void
    renderContent() {
        content = render("layout/header").asDom();
    }

    @Test public void
    logoLinksToHome() {
        assertThat("content", content, hasUniqueSelector("#logo a", hasAttribute("href", "/")));
    }
}
