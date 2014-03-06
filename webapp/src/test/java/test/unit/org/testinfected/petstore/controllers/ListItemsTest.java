package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.FeatureMatcher;
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
import org.testinfected.petstore.views.AvailableItems;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.petstore.builders.Builder;
import test.support.org.testinfected.petstore.web.MockPage;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
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
        itemsPage.assertRenderingContext(availableItems(items));
    }

    private Matcher<AvailableItems> availableItems(Iterable<Item> items) {
        return new FeatureMatcher<AvailableItems, Iterable<Item>>(equalTo(items),
                "available items", "items") {
            protected Iterable<Item> featureValueOf(AvailableItems actual) {
                return actual.getEach();
            }
        };
    }

    private void searchYields(final Builder<Item>... results) {
        this.items.addAll(build(results));

        context.checking(new Expectations() {{
            allowing(itemInventory).findByProductNumber(productNumber); will(returnValue(items));
        }});
    }
}
