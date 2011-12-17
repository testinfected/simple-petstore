package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static org.testinfected.hamcrest.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.cartPath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class MenuPartialTest {

    String MENU_PARTIAL_NAME = "decorators/_menu";
    Element partial;

    @Test public void
    linkIsInactiveWhenCartIsEmpty() {
        partial = renderMenuPartial().using(aModel().with(aCart())).asDom();
        assertThat("partial", partial, hasNoSelector("#shopping-cart a"));
        assertThat("partial", partial, withText(containsString("0")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        partial = renderMenuPartial().using(aModel().with(
                aCart().containing(anItem(), anItem()))
        ).asDom();
        assertThat("partial", partial,
                hasUniqueSelector("#shopping-cart a",
                        withAttribute("href", cartPath()),
                        withText(containsString("2"))));
    }

    private VelocityRendering renderMenuPartial() {
        return render(MENU_PARTIAL_NAME);
    }
}