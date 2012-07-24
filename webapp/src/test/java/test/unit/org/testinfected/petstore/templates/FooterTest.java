package test.unit.org.testinfected.petstore.templates;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChildren;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class FooterTest {

    String FOOTER_TEMPLATE = "footer";
    Element footer;

    @Before public void
    renderFooter() {
        footer = render(FOOTER_TEMPLATE).from(WebRoot.locateLayout()).asDom();
    }

    @SuppressWarnings("unchecked") @Test public void
    logoutButtonSubmitsADeleteToLogoutPath() {
        assertThat("footer", footer, hasLogoutForm(hasAttribute("action", "/logout"), hasAttribute("method", "post")));
        assertThat("footer", footer, hasLogoutForm(hasChildren(
                anElement(hasTag("input"), hasAttribute("type", "hidden"), hasAttribute("name", "_method"), hasAttribute("value", "delete")),
                anElement(hasTag("button")))));
    }

    private Matcher<Element> hasLogoutForm(Matcher<Element>... formMatchers) {
        return hasSelector("#logout-box form", formMatchers);
    }
}
