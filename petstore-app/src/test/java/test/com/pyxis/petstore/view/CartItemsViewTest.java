package test.com.pyxis.petstore.view;

import com.pyxis.petstore.domain.Item;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.com.pyxis.petstore.builders.ItemBuilder;

import java.util.Map;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.velocity.PathFor.homePath;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

public class CartItemsViewTest {

    String CART_ITEMS_VIEW = "cartitems";
    String renderedPage;

    @Test public void
    displaysColumnHeadings() {
        renderedPage = renderCartItemsPageUsing(aModelWith(aCart()));
        assertThat(dom(renderedPage),
                hasSelector("#cart_items th",
                        inOrder(withText("Quantity"),
                                withText("Item"),
                                withText("Price"),
                                withText("Total"))));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        Item item = anItem().withNumber("12345678").priced("18.50").describedAs("Green Adult").build();

        renderedPage = renderCartItemsPageUsing(aModelWith(aCart().with(item).with(item)));
        assertThat(dom(renderedPage),
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
        renderedPage = renderCartItemsPageUsing(aModelWith(aCart().
                with(anItem).
                with(anItem).
                with(anotherItem)));
        assertThat(dom(renderedPage), hasSelector("#cart_items tr.cart-item", withSize(2)));
    }

    @Test public void
    displaysCartGrandTotal() {
        renderedPage = renderCartItemsPageUsing(aModelWith(aCart().
            with(anItem().priced("20.00")).
            with(anItem().priced("12.99")).
            with(anItem().priced("43.97"))));
        String grandTotal = "76.96";

        assertThat(dom(renderedPage), hasUniqueSelector("#cart_items .calculations .total", withText(grandTotal)));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        Map<String, Object> model = aModelWith(
                aCart().with(anItem().priced("20.00").of(aProduct().withNumber("LAB-1234"))));
        renderedPage = renderCartItemsPageUsing(model);
        assertThat(dom(renderedPage), hasUniqueSelector("a#continue-shopping", withAttribute("href", homePath())));
    }

    private Map<String, Object> aModelWith(Builder<?> builder) {
        ModelMap model = new ModelMap();
        model.addAttribute(builder.build());
        return model;
    }

    private String renderCartItemsPageUsing(Map<String, Object> model) {
        return render(CART_ITEMS_VIEW).using(model);
    }
}