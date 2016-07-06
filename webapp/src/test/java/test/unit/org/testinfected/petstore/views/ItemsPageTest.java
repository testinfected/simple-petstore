package test.unit.org.testinfected.petstore.views;

import org.junit.Test;
import org.testinfected.petstore.views.AvailableItems;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static com.vtence.hamcrest.dom.DomMatchers.contains;
import static com.vtence.hamcrest.dom.DomMatchers.hasAttribute;
import static com.vtence.hamcrest.dom.DomMatchers.hasBlankText;
import static com.vtence.hamcrest.dom.DomMatchers.hasChild;
import static com.vtence.hamcrest.dom.DomMatchers.hasId;
import static com.vtence.hamcrest.dom.DomMatchers.hasNoSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasSize;
import static com.vtence.hamcrest.dom.DomMatchers.hasTag;
import static com.vtence.hamcrest.dom.DomMatchers.hasText;
import static com.vtence.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class ItemsPageTest {
    String ITEMS_TEMPLATE = "items";

    Element itemsPage;
    AvailableItems items = new AvailableItems();

    @Test public void
    indicatesWhenNoItemIsAvailable() {
        itemsPage = renderItemsPage().with(items).asDom();

        assertThat("items page", itemsPage, hasUniqueSelector("#out-of-stock"));
        assertThat("items page", itemsPage, hasNoSelector("#inventory"));
    }

    @Test public void
    displaysNumberOfAvailableItems() {
        itemsPage = renderItemsPage().with(items.add(build(anItem(), anItem()))).asDom();

        assertThat("items page", itemsPage, hasUniqueSelector("#item-count", hasText("2")));
        assertThat("items page", itemsPage, hasSelector("#inventory tr[id^='item']", hasSize(2)));
    }

    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        itemsPage = renderItemsPage().with(items.add(build(anItem()))).asDom();

        assertThat("items page", itemsPage,
                hasSelector("#items th",
                        contains(hasText("Reference number"),
                                hasText("Description"),
                                hasText("Price"),
                                hasBlankText())));
    }

    @Test public void
    displaysItemDetailsInColumns() throws Exception {
        itemsPage = renderItemsPage().
                with(items.add(build(anItem().withNumber("12345678").
                        describedAs("Green Adult").priced("18.50")))).asDom();

        assertThat("items page", itemsPage,
                hasSelector("tr#item-12345678 td",
                        contains(hasText("12345678"),
                                hasText("Green Adult"),
                                hasText("18.50"),
                                hasChild(hasTag("form")))));
    }

    @Test public void
    addsItemToShoppingCartWhenFormSubmitted() {
        itemsPage = renderItemsPage().
                with(items.add(build(anItem().withNumber("12345678")))).asDom();

        assertThat("items page", itemsPage,
                hasUniqueSelector("form",
                        hasAttribute("action", "/cart"),
                        hasAttribute("method", "post"),
                        hasUniqueSelector("button", hasId("add-to-cart-12345678"))));
        assertThat("items page", itemsPage,
                hasUniqueSelector("form input[type='hidden']",
                        hasAttribute("name", "item-number"),
                        hasAttribute("value", "12345678")));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        itemsPage = renderItemsPage().with(items.add(build(anItem()))).asDom();
        assertThat("items page", itemsPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    private OfflineRenderer renderItemsPage() {
        return render(ITEMS_TEMPLATE).from(WebRoot.pages());
    }
}
