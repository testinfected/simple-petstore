package test.com.pyxis.petstore.controller;

import com.pyxis.petstore.controller.PurchasesController;
import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.billing.CreditCardType;
import com.pyxis.petstore.domain.billing.PaymentMethod;
import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.CheckoutAssistant;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.PaymentCollector;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

import static com.pyxis.matchers.spring.SpringMatchers.hasAttribute;
import static com.pyxis.matchers.spring.SpringMatchers.isRedirectedTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static test.support.com.pyxis.petstore.builders.AddressBuilder.anAddress;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;
import static test.support.com.pyxis.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.com.pyxis.petstore.builders.ItemBuilder.anItem;
import static test.support.com.pyxis.petstore.builders.OrderBuilder.anOrder;

@RunWith(JMock.class)
public class PurchasesControllerTest {

    Mockery context = new JUnit4Mockery();
    CheckoutAssistant checkoutAssistant = context.mock(CheckoutAssistant.class);
    PaymentCollector paymentCollector = context.mock(PaymentCollector.class);
    Cart cart = aCart().containing(anItem()).build();
    PurchasesController controller = new PurchasesController(cart, checkoutAssistant, paymentCollector);
    Model model = new ExtendedModelMap();
    SessionStatus status = new SimpleSessionStatus();

    @Test public void
    checkoutCartsAndRendersPurchaseForm() {
        String view = controller.checkout(model);
        assertRendersNewPurchaseView(view);
    }

    private void assertRendersNewPurchaseView(String view) {
        assertThat(view, equalTo("purchases/new"));
        assertThat(model, hasAttribute("cart", cart));
        assertThat(model, hasAttribute("cardTypes", CreditCardType.options()));
    }

    @Test public void
    collectsPaymentAndRedirectsToReceiptView() {
        final Order order = anOrder().from(cart).withNumber("12345678").build();

        final CreditCardDetails paymentDetails = aVisa().
                billedTo(anAddress().
                    withFirstName("John").
                    withLastName("Leclair").
                    withEmail("jleclair@gmail.com")).
                withNumber("9999 9999 9999").
                withExpiryDate("12/12").build();

        context.checking(new Expectations() {{
            oneOf(checkoutAssistant).checkout(cart); will(returnValue(order));
            oneOf(paymentCollector).collectPayment(
                    with(equal(order)),
                    with(samePaymentMethodAs(paymentDetails)));
        }});
        String view = controller.create(paymentDetails, bindingFor(paymentDetails), status, model);
        assertThat(view, isRedirectedTo("/receipts/" + order.getNumber()));
        assertTrue(status.isComplete());
    }

    @Test public void
    rendersErrorsOnCheckoutPageIfPaymentDetailsAreNotValid() {
        final CreditCardDetails incompleteDetails = aVisa().build();

        context.checking(new Expectations() {{
            never(checkoutAssistant).checkout(cart);
        }});

        BindingResult result = reportErrorsOn(incompleteDetails);
        String view = controller.create(incompleteDetails, result, status, model);
        assertThat(result, hasGlobalError("invalid"));
        assertFalse(status.isComplete());
        assertRendersNewPurchaseView(view);
    }

    private Matcher<? super BindingResult> hasGlobalError(String errorCode) {
        return new FeatureMatcher<BindingResult, String>(equalTo(errorCode), "a binding result with global error", "error") {
            @Override protected String featureValueOf(BindingResult actual) {
                return actual.getGlobalError() != null ? actual.getGlobalError().getCode() : null;
            }
        };
    }

    private BindingResult reportErrorsOn(CreditCardDetails incompleteDetails) {
        BindingResult binding = bindingFor(incompleteDetails);
        binding.rejectValue("cardNumber", "empty");
        return binding;
    }

    private BindingResult bindingFor(PaymentMethod paymentDetails) {
        return new BeanPropertyBindingResult(paymentDetails, "paymentDetails");
    }

    private Matcher<? extends PaymentMethod> samePaymentMethodAs(CreditCardDetails paymentMethod) {
        return samePropertyValuesAs(paymentMethod);
    }
}
