package test.unit.org.testinfected.petstore.views;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

public class CartPageTest {

    String CART_TEMPLATE = "cart";

    Element cartPage;

    @SuppressWarnings("unchecked")
    @Test public void
    displaysColumnHeadings() {
        cartPage = renderCartPage().withContext(aCart()).asDom();
        assertThat("cart page", cartPage,
                hasSelector("#cart-content th",
                        matches(hasText("Quantity"),
                                hasText("Item"),
                                hasText("Price"),
                                hasText("Total"))));
    }

    @Test public void
    letsReturnToHomePageToContinueShopping() {
        cartPage = renderCartPage().withContext(aCart().containing(anItem())).asDom();
        assertThat("view", cartPage, hasUniqueSelector("a#continue-shopping", hasAttribute("href", "/")));
    }

    private OfflineRenderer renderCartPage() {
        return OfflineRenderer.render(CART_TEMPLATE).from(WebRoot.pages());
    }
}
