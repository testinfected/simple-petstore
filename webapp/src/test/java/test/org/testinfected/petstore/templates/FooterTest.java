package test.org.testinfected.petstore.templates;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChildren;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static test.support.org.testinfected.petstore.templating.OfflineRenderer.render;

public class FooterTest {

    Element content;

    @Before public void
    renderContent() {
        content = render("layout/footer").asDom();
    }

    @SuppressWarnings("unchecked") @Test public void
    logoutButtonSubmitsADeleteToLogoutPath() {
        assertThat("content", content, hasLogoutForm(hasAttribute("action", "/logout"), hasAttribute("method", "post")));
        assertThat("content", content, hasLogoutForm(hasChildren(
                anElement(hasTag("input"), hasAttribute("type", "hidden"), hasAttribute("name", "_method"), hasAttribute("value", "delete")),
                anElement(hasTag("button")))));
    }

    private Matcher<Element> hasLogoutForm(Matcher<Element>... formMatchers) {
        return hasSelector("form#logout", formMatchers);
    }
}
