package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.helpers.ChoiceOfCreditCards;
import org.testinfected.petstore.helpers.Form;
import org.testinfected.petstore.helpers.ListOfErrors;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.AddressBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.testinfected.hamcrest.dom.DomMatchers.anElement;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasChild;
import static org.testinfected.hamcrest.dom.DomMatchers.hasName;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static org.testinfected.hamcrest.dom.DomMatchers.matchesInAnyOrder;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class CheckoutPageTest {

    String CHECKOUT_TEMPLATE = "checkout";
    Element checkoutPage;
    Form.Errors errors = new Form.Errors();

    @Test public void
    displaysOrderSummary() {
        checkoutPage = renderCheckoutPage().with("total", new BigDecimal("250.00")).asDom();
        assertThat("checkout page", checkoutPage, hasUniqueSelector("#cart-grand-total", hasText("250.00")));
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
        checkoutPage = renderCheckoutPage().with("cardTypes", ChoiceOfCreditCards.from(CreditCardType.values())).asDom();
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
        errors.add("paymentDetails", "invalid.paymentDetails");
        errors.add("paymentDetails", "incomplete.paymentDetails");
        errors.add("paymentDetails.cardNumber", "blank.paymentDetails.cardNumber");

        checkoutPage = renderCheckoutPage().with("errors", new ListOfErrors(errors)).asDom();

        assertThat("payment errors", checkoutPage, hasSelector(".errors", allOf(hasChild(
                hasText("invalid.paymentDetails")), hasChild(hasText("incomplete.paymentDetails"))
        )));
        assertThat("card number errors", checkoutPage, hasSelector(".errors", hasChild(
                hasText("blank.paymentDetails.cardNumber")
        )));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    restoresFormValuesWhenAValidationErrorOccurs() throws Exception {
        AddressBuilder billingAddress = anAddress().
                withFirstName("Jack").withLastName("Johnson").withEmail("jack@gmail.com");
        CreditCardDetails payment = aVisa().
                withNumber("4111111111111111").
                withExpiryDate("2015-10-10").
                billedTo(billingAddress).build();

        checkoutPage = renderCheckoutPage().with("payment", payment).
                with("cardTypes", ChoiceOfCreditCards.all().select(payment.cardType())).asDom();

        assertThat("billing information", checkoutPage, hasCheckoutForm(hasBillingInformation("Jack", "Johnson", "jack@gmail.com")));
        assertThat("payment information", checkoutPage, hasCheckoutForm(hasCreditCardDetails(CreditCardType.visa, "4111111111111111", "2015-10-10")));
    }

    private Matcher<? super Element> hasCheckoutForm(Matcher<Element>... formMatchers) {
        return hasUniqueSelector("form#order", formMatchers);
    }

    private Matcher<Element> hasEmptyBillingInformation() {
        return hasBillingInformation("", "", "");
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasBillingInformation(String firstName, String lastName, String email) {
        return hasUniqueSelector("#billing-address", hasInputFields(matches(
                anElement(hasName("first-name"), hasAttribute("value", firstName)),
                anElement(hasName("last-name"), hasAttribute("value", lastName)),
                anElement(hasName("email"), hasAttribute("value", email)))));
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasEmptyPaymentDetails() {
        return hasUniqueSelector("#payment-details", hasSelectionOfCreditCardTypes(), hasEmptyCardNumberAndExpiryDate());
    }

    @SuppressWarnings("unchecked")
    private Matcher<Element> hasCreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate) {
        return hasUniqueSelector("#payment-details",
                hasSelectedCardType(cardType),
                hasCardNumberAndExpiryDate(cardNumber, cardExpiryDate));
    }

    private Matcher<Element> hasInputFields(final Matcher<Iterable<Element>> fieldMatchers) {
        return hasSelector("input[type='text']", fieldMatchers);
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
    private Matcher<Element> hasSelectedCardType(CreditCardType cardType) {
        return hasSelectionList(
                hasName("card-type"), hasChild(anElement(hasAttribute("value", cardType.name()), hasAttribute("selected", "selected"))));
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
        return render(CHECKOUT_TEMPLATE).from(WebRoot.pages());
    }
}