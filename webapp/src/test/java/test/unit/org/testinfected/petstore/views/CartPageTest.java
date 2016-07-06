package test.unit.org.testinfected.petstore.views;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.CartBuilder;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static com.vtence.hamcrest.dom.DomMatchers.contains;
import static com.vtence.hamcrest.dom.DomMatchers.hasAttribute;
import static com.vtence.hamcrest.dom.DomMatchers.hasBlankText;
import static com.vtence.hamcrest.dom.DomMatchers.hasSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasText;
import static com.vtence.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.iterableWithSize;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class CartPageTest {

    String CART_TEMPLATE = "cart";

    Element cartPage;
    CartBuilder cart = aCart();

    @Test public void
    displaysColumnHeadings() {
        cartPage = renderCartPage().with(cart).asDom();
        assertThat("cart page", cartPage,
                hasSelector("#cart-content th",
                        contains(hasText("Quantity"),
                                hasText("Item"),
                                hasText("Price"),
                                hasText("Total"))));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        cartPage = renderCartPage().with(cart).asDom();
        assertThat("cart page", cartPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    @Test public void
    displaysCartGrandTotal() {
        String grandTotal = "76.96";
        cartPage = renderCartPage().with(cart.containing(
                anItem().priced("20.00"), anItem().priced("12.99"),anItem().priced("43.97"))).
                asDom();
        assertThat("cart page", cartPage, hasUniqueSelector("#cart-grand-total", hasText(grandTotal)));
    }

    @Test public void
    displaysOneCartItemPerLine() {
        cartPage = renderCartPage().with(cart.containing(
                anItem().withNumber("12345678"), anItem().withNumber("12345678"),
                anItem().withNumber("87654321"))).asDom();
        assertThat("cart page", cartPage, hasSelector("#cart-content tr[id^='cart-item']", iterableWithSize(2)));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        ItemBuilder item = anItem().withNumber("12345678").priced("18.50").describedAs("Green Adult");
        cartPage = renderCartPage().with(cart.containing(item, item)).asDom();
        assertThat("cart page", cartPage,
                hasSelector("tr#cart-item-12345678 td",
                        contains(hasText("2"),
                                hasText(containsString("Green Adult")),
                                hasText("18.50"),
                                hasText("37.00"),
                                hasBlankText())));
    }

    @Test public void
    rendersPaymentFormToCheckout() {
        cartPage = renderCartPage().with(cart).asDom();
        assertThat("cart page", cartPage, hasUniqueSelector(".confirm a", hasAttribute("href", "/orders/new")));
    }

    private OfflineRenderer renderCartPage() {
        return render(CART_TEMPLATE).from(WebRoot.pages());
    }
}
