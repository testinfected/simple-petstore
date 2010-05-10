package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.cartPath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class CartPartialTest {

    String CART_PARTIAL_NAME = "decorators/_cart";
    Element cartPartial;

    @Test public void
    linkIsInactiveWhenCartIsEmpty() {
        cartPartial = renderCartPartial().using(aModel().with(aCart())).asDom();
        assertThat("partial", cartPartial, hasNoSelector("a"));
        assertThat("partial", cartPartial, withText(containsString("Empty")));
    }

    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        cartPartial = renderCartPartial().using(aModel().with(
                aCart().containing(anItem(), anItem()))
        ).asDom();
        assertThat("partial", cartPartial,
                hasUniqueSelector("a",
                        withAttribute("href", cartPath()),
                        withText(containsString("2 items"))));
    }

    private VelocityRendering renderCartPartial() {
        return render(CART_PARTIAL_NAME);
    }
}