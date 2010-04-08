package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.product.Item;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ItemBuilder;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.PathFor.checkoutPath;
import static test.support.com.pyxis.petstore.views.PathFor.homePath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class CartViewTest {

    String CART_VIEW = "cart";
    String renderedView;

    @Test public void
    displaysColumnHeadings() {
        renderedView = renderCartView().using(aModel().with(aCart()));
        assertThat(dom(renderedView),
                hasSelector("#cart th",
                        inOrder(withText("Quantity"),
                                withText("Item"),
                                withText("Price"),
                                withText("Total"))));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        Item item = anItem().withNumber("12345678").priced("18.50").describedAs("Green Adult").build();

        renderedView = renderCartView().using(aModel().with(aCart().containing(item, item)));
        assertThat(dom(renderedView),
                hasSelector("tr#cart_item_12345678 td",
                        inOrder(withText("2"),
                                withText(containsString("Green Adult")),
                                withText("18.50"),
                                withText("37.00"))));
    }

    @Test public void
    displaysOneCartItemPerLine() {
        ItemBuilder anItem = anItem();
        ItemBuilder anotherItem = anItem();
        renderedView = renderCartView().using(aModel().with(aCart().containing(anItem, anItem, anotherItem)));
        assertThat(dom(renderedView), hasSelector("#cart tr.cart-item", withSize(2)));
    }

    @Test public void
    displaysCartGrandTotal() {
        renderedView = renderCartView().using(aModel().with(aCart().containing(
                anItem().priced("20.00"),
                anItem().priced("12.99"),
                anItem().priced("43.97"))));
        String grandTotal = "76.96";

        assertThat(dom(renderedView), hasUniqueSelector("#cart .calculations .total", withText(grandTotal)));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        renderedView = renderCartView().using(aModel().with(aCart().containing(anItem())));
        assertThat(dom(renderedView), hasUniqueSelector("a#continue-shopping", withAttribute("href", homePath())));
    }

    @Test public void
    checkingOutRendersPaymentForm() {
        renderedView = renderCartView().using(aModel().with(aCart().containing(anItem())));
        assertThat(dom(renderedView), hasUniqueSelector("a#checkout", withAttribute("href", checkoutPath())));
    }

    private VelocityRendering renderCartView() {
        return render(CART_VIEW);
    }
}