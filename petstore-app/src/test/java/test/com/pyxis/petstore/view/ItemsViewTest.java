package test.com.pyxis.petstore.view;

import org.junit.Test;
import test.support.com.pyxis.petstore.views.VelocityRendering;

import static com.pyxis.matchers.dom.DomMatchers.*;
import static com.threelevers.css.DocumentBuilder.dom;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.views.ModelBuilder.aModel;
import static test.support.com.pyxis.petstore.views.ModelBuilder.anEmptyModel;
import static test.support.com.pyxis.petstore.views.PathFor.cartItemsPath;
import static test.support.com.pyxis.petstore.views.VelocityRendering.render;

public class ItemsViewTest {

    String ITEMS_VIEW = "items";
    //todo keep dom representation instead
    String renderedView;

    @Test public void
    notifiesWhenInventoryIsEmpty() {
        renderedView = renderItemsView().using(anEmptyModel());

        assertThat(dom(renderedView), hasUniqueSelector("#out-of-stock"));
        assertThat(dom(renderedView), hasNoSelector("#items"));
    }

    @Test public void
    displaysNumberOfItemsAvailable() {
        renderedView = renderItemsView().using(aModel().listing(anItem(), anItem()));
        assertThat(dom(renderedView), hasUniqueSelector("#inventory-count", withText("2")));
        assertThat(dom(renderedView), hasSelector("#items tr.item", withSize(2)));
    }

    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        renderedView = renderItemsView().using(aModel().listing(anItem()));
        assertThat(dom(renderedView),
                hasSelector("#items th",
                        inOrder(withText("Reference number"),
                                withText("Description"),
                                withText("Price"),
                                withBlankText())));
    }

    @Test public void
    displaysProductDetailsInColumns() throws Exception {
        renderedView = renderItemsView().using(aModel().listing(anItem().
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
        renderedView = renderItemsView().using(aModel().listing(anItem().withNumber("12345678")));
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

    private VelocityRendering renderItemsView() {
        return render(ITEMS_VIEW);
    }
}
