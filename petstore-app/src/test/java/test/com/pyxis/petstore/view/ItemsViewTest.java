package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import test.support.com.pyxis.petstore.builders.Builder;

import java.util.Map;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.Entities.entities;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.velocity.PathFor.cartItemsPath;
import static test.support.com.pyxis.petstore.velocity.VelocityRendering.render;

public class ItemsViewTest {

    String ITEMS_VIEW = "items";
    String renderedView;

    @Test public void
    notifiesWhenInventoryIsEmpty() {
        renderedView = renderItemsViewUsing(anEmptyModel());

        assertThat(dom(renderedView), hasUniqueSelector("#out-of-stock"));
        assertThat(dom(renderedView), hasNoSelector("#items"));
    }

    @Test public void
    displaysNumberOfItemsAvailable() {
        renderedView = renderItemsViewUsing(aModelWith(anItem(), anItem()));
        assertThat(dom(renderedView), hasUniqueSelector("#inventory-count", withText("2")));
        assertThat(dom(renderedView), hasSelector("#items tr.item", withSize(2)));
    }

    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        renderedView = renderItemsViewUsing(aModelWith(anItem()));
        assertThat(dom(renderedView),
                hasSelector("#items th",
                        inOrder(withText("Reference number"),
                                withText("Description"),
                                withText("Price"),
                                withBlankText())));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {

        renderedView = renderItemsViewUsing(aModelWith(anItem().
                withNumber("12345678").
                describedAs("Green Adult").
                priced("18.50")));
        assertThat(dom(renderedView),
                hasSelector("tr#item_12345678 td",
                        inOrder(withText("12345678"),
                                withText("Green Adult"),
                                withText("18.50"),
                                hasChild(withTag("form")))));
    }

    @Test public void
    buttonAddsItemToShoppingCart() {
        renderedView = renderItemsViewUsing(aModelWith(anItem().withNumber("12345678")));
        assertThat(dom(renderedView),
                hasUniqueSelector("form",
                        withAttribute("action", cartItemsPath()),
                        withAttribute("method", "post"),
                        hasUniqueSelector("button", withId("add_to_cart_12345678"))));
        assertThat(dom(renderedView),
                hasUniqueSelector("form input[type='hidden']",
                        withAttribute("name", "item_number"),
                        withAttribute("value", "12345678")));
    }

    private Map<String, Object> anEmptyModel() {
        return aModelWith();
    }

    private Map<String, Object> aModelWith(Builder<?>... builders) {
        ModelMap model = new ModelMap();
        model.addAttribute(entities(builders));
        return model;
    }

    private String renderItemsViewUsing(Map<String, Object> model) {
        return render(ITEMS_VIEW).using(model);
    }
}
