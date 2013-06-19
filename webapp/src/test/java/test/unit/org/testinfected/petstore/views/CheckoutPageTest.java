package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.views.ErrorList;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChildren;
import static org.testinfected.hamcrest.dom.DomMatchers.hasName;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static org.testinfected.hamcrest.dom.DomMatchers.matchesInAnyOrder;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class CheckoutPageTest {

    String CHECKOUT_TEMPLATE = "checkout";
    Element checkoutPage;
    Map<String, List<String>> errors = new HashMap<String, List<String>>();

    @Test public void
    displaysOrderSummary() {
        checkoutPage = renderCheckoutPage().asDom();
        assertThat("checkout page", checkoutPage, hasUniqueSelector("#cart-grand-total", hasText("100.00")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysPurchaseForm() {
        checkoutPage = renderCheckoutPage().asDom();
        assertThat("checkout page", checkoutPage, hasCheckoutForm(anElement(
                hasAttribute("action", "/orders"),
                hasAttribute("method", "post")
        )));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasEmptyBillingInformation()));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasEmptyPaymentDetails()));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasSubmitButton()));
    }

    @Test public void
    fillsCardTypeSelectionList() {
        checkoutPage = renderCheckoutPage().asDom();
        assertThat("checkout page", checkoutPage, hasSelector("#card-type option", hasCreditCardOptions()));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        checkoutPage = renderCheckoutPage().asDom();
        assertThat("checkout page", checkoutPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    rendersErrorsWhenPaymentDetailsAreInvalid() throws Exception {
        errors.put("order", asList("invalid.order", "incomplete.order"));
        errors.put("order.cardNumber", asList("empty.order.cardNumber"));

        checkoutPage = renderCheckoutPage().asDom();

        assertThat("order errors", checkoutPage, hasSelector(".errors", hasChildren(
                hasText("invalid.order"),
                hasText("incomplete.order")
        )));
        assertThat("card number errors", checkoutPage, hasSelector(".errors", hasChild(
                hasText("empty.order.cardNumber")
        )));
    }

    private Matcher<? super Element> hasCheckoutForm(Matcher<Element> formMatcher) {
        return hasUniqueSelector("form#order", formMatcher);
    }

    private Matcher<Element> hasEmptyBillingInformation() {
        return hasBillingInformation("", "", "");
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasBillingInformation(String firstName, String lastName, String email) {
        return hasSelector("#billing-address", hasInputFields(matches(
                anElement(hasName("first-name"), hasAttribute("value", firstName)),
                anElement(hasName("last-name"), hasAttribute("value", lastName)),
                anElement(hasName("email"), hasAttribute("value", email)))));
    }

    private Matcher<Element> hasInputFields(final Matcher<Iterable<Element>> fieldMatchers) {
        return hasSelector("input[type='text']", fieldMatchers);
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasEmptyPaymentDetails() {
        return hasUniqueSelector("#payment-details", hasSelectionOfCreditCardTypes(), hasEmptyCardNumberAndExpiryDate());
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasSelectionOfCreditCardTypes() {
        return hasSelectionList(hasName("card-type"));
    }

    private Matcher<Element> hasSelectionList(final Matcher<Element>... dropDownMatchers) {
        return hasUniqueSelector("select", dropDownMatchers);
    }

    private Matcher<Element> hasEmptyCardNumberAndExpiryDate() {
        return hasCardNumberAndExpiryDate("", "");
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasCardNumberAndExpiryDate(String cardNumber, String cardExpiryDate) {
        return hasInputFields(matches(
                anElement(hasName("card-number"), hasAttribute("value", cardNumber)),
                anElement(hasName("expiry-date"), hasAttribute("value", cardExpiryDate))));
    }

    private Matcher<Element> hasSubmitButton() {
        return hasUniqueSelector("button.confirm");
    }

    private Matcher<Iterable<Element>> hasCreditCardOptions() {
        List<Matcher<? super Element>> matchers = new ArrayList<Matcher<? super Element>>();
        for (CreditCardType type : CreditCardType.values()) {
            matchers.add(hasOption(type.name(), type.commonName()));
        }
        return matchesInAnyOrder(matchers);
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasOption(String value, String text) {
        return anElement(hasAttribute("value", value), hasText(text));
    }

    private OfflineRenderer renderCheckoutPage() {
        return render(CHECKOUT_TEMPLATE).
                with("total", new BigDecimal("100.00")).
                and("cardTypes", CreditCardType.options().entrySet()).
                and("errors", new ErrorList(errors)).
                from(WebRoot.pages());
    }
}