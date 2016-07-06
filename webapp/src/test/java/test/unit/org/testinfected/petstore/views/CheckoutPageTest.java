package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.lib.ErrorMessages;
import org.testinfected.petstore.views.Checkout;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.AddressBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.vtence.hamcrest.dom.DomMatchers.anElement;
import static com.vtence.hamcrest.dom.DomMatchers.contains;
import static com.vtence.hamcrest.dom.DomMatchers.containsInAnyOrder;
import static com.vtence.hamcrest.dom.DomMatchers.hasAttribute;
import static com.vtence.hamcrest.dom.DomMatchers.hasChild;
import static com.vtence.hamcrest.dom.DomMatchers.hasName;
import static com.vtence.hamcrest.dom.DomMatchers.hasSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasText;
import static com.vtence.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class CheckoutPageTest {

    String CHECKOUT_TEMPLATE = "checkout";
    Element checkoutPage;
    Checkout checkout = new Checkout();

    @Test public void
    displaysOrderSummary() {
        checkoutPage = renderCheckoutPage().with(checkout.forTotalOf(new BigDecimal("250.00"))).asDom();
        assertThat("checkout page", checkoutPage, hasUniqueSelector("#cart-grand-total", hasText("250.00")));
    }

    @Test public void
    displaysPurchaseForm() {
        checkoutPage = renderCheckoutPage().with(checkout).asDom();
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
        checkoutPage = renderCheckoutPage().with(checkout).asDom();
        assertThat("checkout page", checkoutPage, hasSelector("#card-type option", hasCreditCardOptions()));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        checkoutPage = renderCheckoutPage().with(checkout).asDom();
        assertThat("checkout page", checkoutPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    @Test public void
    rendersValidationErrors() throws Exception {
        ErrorMessages errors = new ErrorMessages();
        errors.add("paymentDetails", "invalid.paymentDetails");
        errors.add("paymentDetails.cardNumber", "empty.paymentDetails.cardNumber");
        errors.add("paymentDetails.cardNumber", "incorrect.paymentDetails.cardNumber");

        checkoutPage = renderCheckoutPage().with(checkout.withErrors(errors)).asDom();

        assertThat("payment errors", checkoutPage, hasSelector(".errors", anElement(hasChild(
                hasText("invalid.paymentDetails"))
        )));
        assertThat("card number errors", checkoutPage, hasSelector(".errors", allOf(hasChild(
                hasText("empty.paymentDetails.cardNumber")), hasChild(hasText("incorrect.paymentDetails.cardNumber")))));
    }

    @Test public void
    restoresFormValues() throws Exception {
        AddressBuilder billingAddress = anAddress().
                withFirstName("Jack").withLastName("Johnson").withEmail("jack@gmail.com");
        CreditCardDetails paymentDetails = aVisa().
                withNumber("4111111111111111").
                withExpiryDate("2015-10-10").
                billedTo(billingAddress).build();

        checkoutPage = renderCheckoutPage().with(checkout.withPayment(paymentDetails)).asDom();

        assertThat("billing information", checkoutPage, hasCheckoutForm(hasBillingInformation("Jack", "Johnson", "jack@gmail.com")));
        assertThat("payment information", checkoutPage, hasCheckoutForm(hasCreditCardDetails(CreditCardType.visa, "4111111111111111", "2015-10-10")));
    }

    @SafeVarargs
    private final Matcher<? super Element> hasCheckoutForm(Matcher<Element>... formMatchers) {
        return hasUniqueSelector("form#order", formMatchers);
    }

    private Matcher<Element> hasEmptyBillingInformation() {
        return hasBillingInformation("", "", "");
    }

    private Matcher<Element> hasBillingInformation(String firstName, String lastName, String email) {
        return hasUniqueSelector("#billing-address", hasInputFields(contains(
                anElement(hasName("first-name"), hasAttribute("value", firstName)),
                anElement(hasName("last-name"), hasAttribute("value", lastName)),
                anElement(hasName("email"), hasAttribute("value", email)))));
    }

    private Matcher<Element> hasEmptyPaymentDetails() {
        return hasUniqueSelector("#payment-details", hasSelectionOfCreditCardTypes(), hasEmptyCardNumberAndExpiryDate());
    }

    private Matcher<Element> hasCreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate) {
        return hasUniqueSelector("#payment-details",
                hasSelectedCardType(cardType),
                hasCardNumberAndExpiryDate(cardNumber, cardExpiryDate));
    }

    private Matcher<Element> hasInputFields(final Matcher<Iterable<Element>> fieldMatchers) {
        return hasSelector("input[type='text']", fieldMatchers);
    }

    private Matcher<Element> hasSelectionOfCreditCardTypes() {
        return hasSelectionList(hasName("card-type"));
    }

    @SafeVarargs
    private final Matcher<Element> hasSelectionList(final Matcher<Element>... dropDownMatchers) {
        return hasUniqueSelector("select", dropDownMatchers);
    }

    private Matcher<Element> hasEmptyCardNumberAndExpiryDate() {
        return hasCardNumberAndExpiryDate("", "");
    }

    private Matcher<Element> hasSelectedCardType(CreditCardType cardType) {
        return hasSelectionList(
                hasName("card-type"), hasUniqueSelector("option[selected]", hasAttribute("value", cardType.name())));
    }

    private Matcher<Element> hasCardNumberAndExpiryDate(String cardNumber, String cardExpiryDate) {
        return hasInputFields(contains(
                anElement(hasName("card-number"), hasAttribute("value", cardNumber)),
                anElement(hasName("expiry-date"), hasAttribute("value", cardExpiryDate))));
    }

    private Matcher<Element> hasSubmitButton() {
        return hasUniqueSelector("button[type=submit].confirm");
    }

    private Matcher<Iterable<Element>> hasCreditCardOptions() {
        List<Matcher<? super Element>> matchers = new ArrayList<>();
        for (CreditCardType type : CreditCardType.values()) {
            matchers.add(hasOption(type.name(), type.commonName()));
        }
        return containsInAnyOrder(matchers);
    }

    private Matcher<Element> hasOption(String value, String text) {
        return anElement(hasAttribute("value", value), hasText(text));
    }

    private OfflineRenderer renderCheckoutPage() {
        return render(CHECKOUT_TEMPLATE).from(WebRoot.pages());
    }
}