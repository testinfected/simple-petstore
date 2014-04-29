package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
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
    rendersAvailableItemsMatchingProductNumber() throws Exception {
        searchYields(anItem().of(aProduct().withNumber(productNumber)));
        listItems.handle(request, response);
        itemsPage.assertRenderedWith(availableItems(items));
    }

    private Matcher<Object> availableItems(Iterable<Item> items) {
        return hasProperty("each", equalTo(items));
    }

    private void searchYields(final Builder<Item>... results) {
        this.items.addAll(build(results));

        context.checking(new Expectations() {{
            allowing(itemInventory).findByProductNumber(productNumber); will(returnValue(items));
        }});
    }
}
