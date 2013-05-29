package test.unit.org.testinfected.petstore.controllers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.controllers.Checkout;
import org.testinfected.petstore.order.Cart;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.math.BigDecimal;
import java.util.Map;

import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;

@RunWith(JMock.class)
public class CheckoutTest {
    Mockery context = new JUnit4Mockery();
    Page checkoutPage = context.mock(Page.class);
    Checkout checkout = new Checkout(checkoutPage);

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @SuppressWarnings("unchecked") @Test public void
    makesCartAndSupportedCardTypesAvailableToView() throws Exception {
        final BigDecimal total = new BigDecimal("324.98");
        request.session().put(Cart.class, aCart().containing(anItem().priced(total)).build());
        final Map<CreditCardType, String> cardTypes = CreditCardType.options();

        context.checking(new Expectations() {{
            oneOf(checkoutPage).render(with(response), with(allOf(hasEntry("total", total), hasEntry("cardTypes", cardTypes.entrySet()))));
        }});

        checkout.handle(request, response);
    }

    private Matcher<Map<String, Object>> allOf(final Matcher<? super Map<String, Object>>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
