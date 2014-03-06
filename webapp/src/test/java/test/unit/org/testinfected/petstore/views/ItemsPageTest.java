package test.unit.org.testinfected.petstore.views;

import org.junit.Test;
import org.testinfected.petstore.views.AvailableItems;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasBlankText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasId;
import static org.testinfected.hamcrest.dom.DomMatchers.hasNoSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasTag;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
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

    @SuppressWarnings("unchecked")
    @Test public void
    displaysNumberOfAvailableItems() {
        itemsPage = renderItemsPage().with(items.add(build(anItem(), anItem()))).asDom();

        assertThat("items page", itemsPage, hasUniqueSelector("#item-count", hasText("2")));
        assertThat("items page", itemsPage, hasSelector("#inventory tr[id^='item']", hasSize(2)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysColumnHeadingsOnItemsTable() {
        itemsPage = renderItemsPage().with(items.add(build(anItem()))).asDom();

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
        itemsPage = renderItemsPage().
                with(items.add(build(anItem().withNumber("12345678").
                        describedAs("Green Adult").priced("18.50")))).asDom();

        assertThat("items page", itemsPage,
                hasSelector("tr#item-12345678 td",
                        matches(hasText("12345678"),
                                hasText("Green Adult"),
                                hasText("18.50"),
                                hasChild(hasTag("form")))));
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
    @Test public void
    returnsToHomePageToContinueShopping() {
        itemsPage = renderItemsPage().with(items.add(build(anItem()))).asDom();
        assertThat("items page", itemsPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    private OfflineRenderer renderItemsPage() {
        return render(ITEMS_TEMPLATE).from(WebRoot.pages());
    }
}
