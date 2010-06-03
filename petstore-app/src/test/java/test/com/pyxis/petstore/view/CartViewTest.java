package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.product.Item;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.ItemBuilder;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.checkoutPath;
import static test.support.com.pyxis.petstore.views.PathFor.homePath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class CartViewTest {

    String CART_VIEW_NAME = "cart";
    Element cartView;

    @Test public void
    displaysColumnHeadings() {
        cartView = renderCartView().using(aModel().with(aCart())).asDom();
        assertThat("view", cartView,
                hasSelector("#cart th",
                        inOrder(withText("Quantity"),
                                withText("Item"),
                                withText("Price"),
                                withText("Total"))));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        Item item = anItem().withNumber("12345678").priced("18.50").describedAs("Green Adult").build();

        cartView = renderCartView().using(aModel().with(aCart().containing(item, item))).asDom();
        assertThat("view", cartView,
                hasSelector("tr#cart-item-12345678 td",
                        inOrder(withText("2"),
                                withText(containsString("Green Adult")),
                                withText("18.50"),
                                withText("37.00"),
                                withBlankText())));
    }

    @Test public void
    displaysOneCartItemPerLine() {
        ItemBuilder anItem = anItem();
        ItemBuilder anotherItem = anItem();
        cartView = renderCartView().using(aModel().with(aCart().containing(anItem, anItem, anotherItem))).asDom();
        assertThat("view", cartView, hasSelector("#cart tr[id^='cart-item']", withSize(2)));
    }

    @Test public void
    displaysCartGrandTotal() {
        cartView = renderCartView().using(aModel().with(aCart().containing(
                anItem().priced("20.00"),
                anItem().priced("12.99"),
                anItem().priced("43.97")))).asDom();
        String grandTotal = "76.96";

        assertThat("view", cartView, hasUniqueSelector("#cart-grand-total", withText(grandTotal)));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        cartView = renderCartView().using(aModel().with(aCart().containing(anItem()))).asDom();
        assertThat("view", cartView, hasUniqueSelector("a#continue-shopping", withAttribute("href", homePath())));
    }

    @Test public void
    checkingOutRendersPaymentForm() {
        cartView = renderCartView().using(aModel().with(aCart().containing(anItem()))).asDom();
        assertThat("view", cartView, hasUniqueSelector("a#checkout", withAttribute("href", checkoutPath())));
    }

    private VelocityRendering renderCartView() {
        return render(CART_VIEW_NAME);
    }
}