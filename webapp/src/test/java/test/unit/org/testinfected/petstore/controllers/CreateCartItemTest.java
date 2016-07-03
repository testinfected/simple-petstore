package test.unit.org.testinfected.petstore.controllers;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.session.Session;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.order.CartItem;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;

import static com.vtence.molecule.testing.ResponseAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

public class CreateCartItemTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ItemInventory inventory = context.mock(ItemInventory.class);
    CreateCartItem createCartItem = new CreateCartItem(inventory);

    Request request = new Request();
    Response response = new Response();

    String itemNumber = "12345678";

    @Before public void
    createSession() {
        new Session().bind(request);
    }

    @Test public void
    createsCartAndAddsItemToCartBeforeRedirectingToCartPage() throws Exception {
        request.addParameter("item-number", itemNumber);
        final Item item = anItem().withNumber(itemNumber).build();
        inventoryContains(item);

        createCartItem.handle(request, response);

        assertThat(response).isRedirectedTo("/cart");
        assertThat("cart content", cart().getItems(), containsItems(itemWith(number(itemNumber), quantity(1))));
    }

    private void inventoryContains(final Item item) {
        context.checking(new Expectations() {{
            allowing(inventory).find(new ItemNumber(item.getNumber())); will(returnValue(item));
        }});
    }

    private Session session() {
        return Session.get(request);
    }

    private Cart cart() {
        return session().get(Cart.class);
    }

    @SafeVarargs
    private final Matcher<Iterable<CartItem>> containsItems(Matcher<? super CartItem>... cartItemMatchers) {
        return hasItems(cartItemMatchers);
    }

    @SafeVarargs
    private final Matcher<CartItem> itemWith(Matcher<CartItem>... itemMatchers) {
        return allOf(itemMatchers);
    }

    private Matcher<CartItem> quantity(int count) {
        return new FeatureMatcher<CartItem, Integer>(equalTo(count), "an item with quantity", "item quantity") {
            protected Integer featureValueOf(CartItem actual) {
                return actual.getQuantity();
            }
        };
    }

    private Matcher<CartItem> number(String number) {
        return new FeatureMatcher<CartItem, String>(equalTo(number), "an item with number", "item number") {
            protected String featureValueOf(CartItem actual) {
                return actual.getItemNumber();
            }
        };
    }
}
