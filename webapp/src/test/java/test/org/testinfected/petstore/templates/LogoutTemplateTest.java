package test.org.testinfected.petstore.templates;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.templating.Renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static test.support.org.testinfected.petstore.templating.Renderer.render;

public class LogoutTemplateTest {

    Element content;

    @SuppressWarnings("unchecked")
    @Test public void
    displaysLogoutForm() {
        content = renderLogoutTemplate().asDom();
        assertThat("content", content, hasLogoutForm(hasAttribute("action", "/logout"), hasAttribute("method", "post")));
        assertThat("content", content, hasLogoutForm(hasChild(
                anElement(hasTag("input"), hasAttribute("type", "hidden"), hasAttribute("name", "_method"), hasAttribute("value", "delete")))));
    }

    private Matcher<Element> hasLogoutForm(Matcher<Element>... formMatchers) {
        return hasSelector("form#logout", formMatchers);
    }

    private Renderer renderLogoutTemplate() {
        return render("layout/_logout");
    }
}
