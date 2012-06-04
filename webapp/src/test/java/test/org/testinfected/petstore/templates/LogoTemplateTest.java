package test.org.testinfected.petstore.templates;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.templating.Renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.org.testinfected.petstore.templating.Renderer.render;

public class LogoTemplateTest {

    Element content;

    @Test public void
    linksToHome() {
        content = renderLogoPartial().asDom();
        assertThat("content", content, hasUniqueSelector("a", hasAttribute("href", "/")));
    }

    private Renderer renderLogoPartial() {
        return render("layout/_logo");
    }
}
