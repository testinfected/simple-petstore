package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import test.support.com.pyxis.petstore.builders.Builder;

import java.util.Map;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.velocity.PathFor.cartItemsPath;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

public class CartPartialTest {

    String CART_LINK_FRAGMENT = "includes/_cart";
    String renderedFragment;

    @Test public void
    linkIsInactiveWhenCartIsEmpty() {
        renderedFragment = renderCartLinkFragmentUsing(aModelWith(aCart()));
        assertThat(dom(renderedFragment), hasNoSelector("a"));
        assertThat(dom(renderedFragment), withText(containsString("Empty")));
    }

    @Test public void
    displaysTotalItemsInCartAndLinksToCart() throws Exception {
        renderedFragment = renderCartLinkFragmentUsing(
                aModelWith(aCart().with(anItem()).with(anItem())));
        assertThat(dom(renderedFragment),
                hasUniqueSelector("a",
                        withAttribute("href", cartItemsPath()),
                        withText(containsString("2 items"))));
    }

    private Map<String, Object> aModelWith(Builder<?> builder) {
        ModelMap model = new ModelMap();
        model.addAttribute(builder.build());
        return model;
    }

    private String renderCartLinkFragmentUsing(Map<String, Object> model) {
        return render(CART_LINK_FRAGMENT).using(model);
    }
}