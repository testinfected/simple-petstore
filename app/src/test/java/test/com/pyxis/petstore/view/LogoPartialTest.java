package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.Routes;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class LogoPartialTest {

    Routes routes = Routes.toPetstore();
    String LOGO_PARTIAL_NAME = "decorators/_logo";
    Element partial;

    @Test public void
    linksToHomePage() {
        partial = renderLogoPartial().asDom();
        assertThat("partial", partial, hasUniqueSelector("a", hasAttribute("href", routes.homePath())));
    }

    private VelocityRendering renderLogoPartial() {
        return render(LOGO_PARTIAL_NAME).using(routes);
    }
}