package test.unit.org.testinfected.petstore.views;

import com.pyxis.petstore.domain.billing.CreditCardType;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasName;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static org.testinfected.hamcrest.dom.DomMatchers.matchesInAnyOrder;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class CheckoutPageTest {

    String CHECKOUT_TEMPLATE = "checkout";
    Element checkoutPage;

    @Before public void
    renderCheckoutPage() {
        checkoutPage = render(CHECKOUT_TEMPLATE).
                with("cart", aCart().containing(anItem().priced("100.00"))).
                and("cardTypes", CreditCardType.options().entrySet()).from(WebRoot.pages()).asDom();
    }

    @Test public void
    displaysOrderSummary() {
        assertThat("checkout page", checkoutPage, hasUniqueSelector("#cart-grand-total", hasText("100.00")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysPurchaseForm() {
        assertThat("checkout page", checkoutPage, hasCheckoutForm(anElement(
                hasAttribute("action", "/checkout"),
                hasAttribute("method", "post")
        )));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasEmptyBillingInformation()));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasEmptyPaymentDetails()));
        assertThat("checkout page", checkoutPage, hasCheckoutForm(hasSubmitOrderButton()));
    }

    @Test public void
    fillsCardTypeSelectionList() {
        assertThat("checkout page", checkoutPage, hasSelector("#card-type option", hasCreditCardOptions()));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        assertThat("checkout page", checkoutPage, hasUniqueSelector("a#continue-shopping", hasAttribute("href", "/")));
    }

    private Matcher<? super Element> hasCheckoutForm(Matcher<Element> formMatcher) {
        return hasUniqueSelector("form", formMatcher);
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

    private Matcher<Element> hasSubmitOrderButton() {
        return hasUniqueSelector("button#order");
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

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasCreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate) {
        return anElement(
                hasSelectedCardType(cardType),
                hasCardNumberAndExpiryDate(cardNumber, cardExpiryDate));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasSelectedCardType(CreditCardType cardType) {
        return hasSelectionList(
                hasName("card-type"), hasChild(anElement(hasAttribute("value", cardType.toString()), hasAttribute("selected", "selected"))));
    }
}