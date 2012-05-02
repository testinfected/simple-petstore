package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.Routes;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasNoSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class MenuPartialTest {

    Routes routes =Routes.toPetstore();
    String MENU_PARTIAL_NAME = "decorators/_menu";
    Element menuPartial;

    @Test public void
    linkIsInactiveWhenCartIsEmpty() {
        menuPartial = renderMenuPartial().using(aModel().with(aCart())).asDom();
        assertThat("partial", menuPartial, hasNoSelector("#shopping-cart a"));
        assertThat("partial", menuPartial, hasText(containsString("0")));
    }

    @Test public void
    linksToHomePage() {
        menuPartial = renderMenuPartial().asDom();
        assertThat("partial", menuPartial, hasUniqueSelector("#home a",
                hasAttribute("href", routes.homePath())));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        menuPartial = renderMenuPartial().using(aModel().with(
                aCart().containing(anItem(), anItem()))
        ).asDom();
        assertThat("partial", menuPartial,
                hasUniqueSelector("#shopping-cart a",
                        hasAttribute("href", routes.cartPath()),
                        hasText(containsString("2"))));
    }

    private VelocityRendering renderMenuPartial() {
        return render(MENU_PARTIAL_NAME).using(routes);
    }
}