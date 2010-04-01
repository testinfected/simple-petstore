package test.com.pyxis.petstore.view;

import org.junit.Test;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.cartPath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class CartPartialTest {

    String CART_PARTIAL = "decorators/_cart";
    String renderedPartial;

    @Test public void
    linkIsInactiveWhenCartIsEmpty() {
        renderedPartial = renderCartPartial().using(aModel().with(aCart()));
        assertThat(dom(renderedPartial), hasNoSelector("a"));
        assertThat(dom(renderedPartial), withText(containsString("Empty")));
    }

    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        renderedPartial = renderCartPartial().using(aModel().with(aCart().containing(anItem(), anItem())));
        assertThat(dom(renderedPartial),
                hasUniqueSelector("a",
                        withAttribute("href", cartPath()),
                        withText(containsString("2 items"))));
    }

    private VelocityRendering renderCartPartial() {
        return render(CART_PARTIAL);
    }
}