package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.util.ArrayList;
import java.util.List;

import static test.support.org.testinfected.petstore.builders.Builders.build;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class ListItemsTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ItemInventory itemInventory = context.mock(ItemInventory.class);
    MockPage itemsPage = new MockPage();
    ListItems listItems = new ListItems(itemInventory, itemsPage);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    List<Item> items = new ArrayList<Item>();
    String productNumber = "LAB-1234";

    @Before public void
    addProductNumberToRequest() {
        request.addParameter("product", productNumber);
    }

    @After public void
    assertPageRendered() {
        itemsPage.assertRenderedTo(response);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersItemsInInventoryMatchingProductNumber() throws Exception {
        searchYields(anItem().of(aProduct().withNumber(productNumber)));
        listItems.handle(request, response);
        itemsPage.assertRenderedWith("in-stock", true);
        itemsPage.assertRenderedWith("items", items);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    indicatesNoMatchWhenNoProductItemIsAvailable() throws Exception {
        searchYieldsNothing();
        listItems.handle(request, response);
        itemsPage.assertRenderedWith("in-stock", false);
        itemsPage.assertRenderedWith("items", items);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    makesMatchCountAvailableToView() throws Exception {
        searchYields(anItem(), anItem(), anItem());
        listItems.handle(request, response);
        itemsPage.assertRenderedWith("item-count", 3);
    }

    @SuppressWarnings("unchecked")
    private void searchYieldsNothing() {
        searchYields();
    }

    private void searchYields(final Builder<Item>... results) {
        this.items.addAll(build(results));

        context.checking(new Expectations() {{
            allowing(itemInventory).findByProductNumber(productNumber); will(returnValue(items));
        }});
    }
}
