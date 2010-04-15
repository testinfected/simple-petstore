package test.com.pyxis.petstore.view;

import org.junit.Test;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.ModelBuilder.anEmptyModel;
import static test.support.com.pyxis.petstore.views.PathFor.cartItemsPath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class ItemsViewTest {

    String ITEMS_VIEW_NAME = "items";
    Element itemsView;

    @Test public void
    notifiesWhenInventoryIsEmpty() {
        itemsView = renderItemsView().using(anEmptyModel()).asDom();

        assertThat(itemsView, hasUniqueSelector("#out-of-stock"));
        assertThat(itemsView, hasNoSelector("#items"));
    }

    @Test public void
    displaysNumberOfItemsAvailable() {
        itemsView = renderItemsView().using(aModel().listing(anItem(), anItem())).asDom();
        assertThat(itemsView, hasUniqueSelector("#inventory-count", withText("2")));
        assertThat(itemsView, hasSelector("#items tr.item", withSize(2)));
    }

    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        itemsView = renderItemsView().using(aModel().listing(anItem())).asDom();
        assertThat(itemsView,
                hasSelector("#items th",
                        inOrder(withText("Reference number"),
                                withText("Description"),
                                withText("Price"),
                                withBlankText())));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        itemsView = renderItemsView().using(aModel().listing(anItem().
                withNumber("12345678").
                describedAs("Green Adult").
                priced("18.50"))).asDom();
        assertThat(itemsView,
                hasSelector("tr#item-12345678 td",
                        inOrder(withText("12345678"),
                                withText("Green Adult"),
                                withText("18.50"),
                                hasChild(withTag("form")))));
    }

    @Test public void
    buttonAddsItemToShoppingCart() {
        itemsView = renderItemsView().using(aModel().listing(anItem().withNumber("12345678"))).asDom();
        assertThat(itemsView,
                hasUniqueSelector("form",
                        withAttribute("action", cartItemsPath()),
                        withAttribute("method", "post"),
                        hasUniqueSelector("button", withId("add-to-cart-12345678"))));
        assertThat(itemsView,
                hasUniqueSelector("form input[type='hidden']",
                        withAttribute("name", "item_number"),
                        withAttribute("value", "12345678")));
    }

    private VelocityRendering renderItemsView() {
        return render(ITEMS_VIEW_NAME);
    }
}