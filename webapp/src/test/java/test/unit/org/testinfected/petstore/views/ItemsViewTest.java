package test.unit.org.testinfected.petstore.views;

import com.pyxis.petstore.domain.product.Item;
import org.junit.Test;
import org.testinfected.petstore.util.Context;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasNoSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.petstore.util.Context.context;

public class ItemsViewTest {
    String ITEMS_TEMPLATE = "items";

    Element itemsView;
    List<Item> items = new ArrayList<Item>();
    Context context = context().with("items", items);


    @Test public void
    indicatesWhenNoItemIsAvailable() {
        itemsView = renderItemsView().asDom();
        assertThat("view", itemsView, hasUniqueSelector("#out-of-stock"));
        assertThat("view", itemsView, hasNoSelector("#inventory"));
    }

    private OfflineRenderer renderItemsView() {
        return OfflineRenderer.render(ITEMS_TEMPLATE).using(context.with("match-found", !items.isEmpty())).from(WebRoot.pages());
    }
}
