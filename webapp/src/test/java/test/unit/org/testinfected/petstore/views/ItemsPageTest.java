package test.unit.org.testinfected.petstore.views;

import com.pyxis.petstore.domain.product.Item;
import org.junit.Test;
import org.testinfected.petstore.util.Context;
import org.w3c.dom.Element;
import test.support.com.pyxis.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasBlankText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasNoSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static org.testinfected.petstore.util.Context.context;
import static test.support.com.pyxis.petstore.builders.Builders.build;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;

public class ItemsPageTest {
    String ITEMS_TEMPLATE = "items";

    Element itemsPage;
    List<Item> itemAvailable = new ArrayList<Item>();
    Context context = context().with("items", itemAvailable);

    @Test public void
    indicatesWhenNoItemIsAvailable() {
        itemsPage = renderItemsPage().asDom();

        assertThat("items page", itemsPage, hasUniqueSelector("#out-of-stock"));
        assertThat("items page", itemsPage, hasNoSelector("#inventory"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysNumberOfItemsAvailable() {
        addAsAvailable(anItem(), anItem());

        itemsPage = renderItemsPage().using("item-count", 2).asDom();

        assertThat("items page", itemsPage, hasUniqueSelector("#item-count", hasText("2")));
        assertThat("items page", itemsPage, hasSelector("#inventory tr[id^='item']", hasSize(2)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        addAsAvailable(anItem());

        itemsPage = renderItemsPage().asDom();

        assertThat("items page", itemsPage,
                hasSelector("#items th",
                        matches(hasText("Reference number"),
                                hasText("Description"),
                                hasText("Price"),
                                hasBlankText())));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysItemDetailsInColumns() throws Exception {
        addAsAvailable(anItem().withNumber("12345678").describedAs("Green Adult").priced("18.50"));

        itemsPage = renderItemsPage().asDom();

        assertThat("items page", itemsPage,
                hasSelector("tr#item-12345678 td",
                        matches(hasText("12345678"),
                                hasText("Green Adult"),
                                hasText("18.50"),
                                hasChild(hasTag("form")))));
    }

    private void addAsAvailable(Builder<Item>... items) {
        this.itemAvailable.addAll(build(items));
    }

    private OfflineRenderer renderItemsPage() {
        return OfflineRenderer.render(ITEMS_TEMPLATE).using(context.with("in-stock", !itemAvailable.isEmpty())).from(WebRoot.pages());
    }
}
