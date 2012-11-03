package test.unit.org.testinfected.petstore.views;

import com.pyxis.petstore.domain.product.Item;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasBlankText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
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
        assertThat("cart page", cartPage, hasUniqueSelector("a#continue-shopping", hasAttribute("href", "/")));
    }

    @Test public void
    displaysCartGrandTotal() {
        cartPage = renderCartPage().withContext(aCart().containing(
                anItem().priced("20.00"),
                anItem().priced("12.99"),
                anItem().priced("43.97"))).asDom();
        String grandTotal = "76.96";

        assertThat("cart page", cartPage, hasUniqueSelector("#cart-grand-total", hasText(grandTotal)));
    }

    @Test public void
    displaysOneCartItemPerLine() {
        ItemBuilder anItem = anItem();
        ItemBuilder anotherItem = anItem();
        cartPage = renderCartPage().withContext(aCart().containing(anItem, anItem, anotherItem)).asDom();
        assertThat("cart page", cartPage, hasSelector("#cart-content tr[id^='cart-item']", hasSize(2)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        Item item = anItem().withNumber("12345678").priced("18.50").describedAs("Green Adult").build();

        cartPage = renderCartPage().withContext(aCart().containing(item, item)).asDom();
        assertThat("cart page", cartPage,
                hasSelector("tr#cart-item-12345678 td",
                        matches(hasText("2"),
                                hasText(containsString("Green Adult")),
                                hasText("18.50"),
                                hasText("37.00"),
                                hasBlankText())));
    }

    @Test public void
    rendersPaymentFormToCheckout() {
        cartPage = renderCartPage().withContext(aCart().containing(anItem())).asDom();
        assertThat("cart page", cartPage, hasUniqueSelector("#checkout a", hasAttribute("href", "/checkout")));
    }

    private OfflineRenderer renderCartPage() {
        return OfflineRenderer.render(CART_TEMPLATE).from(WebRoot.pages());
    }
}
