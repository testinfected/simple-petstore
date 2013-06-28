package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.builders.OrderBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testinfected.hamcrest.dom.DomMatchers.hasAttribute;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.hasSize;
import static org.testinfected.hamcrest.dom.DomMatchers.hasText;
import static org.testinfected.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.testinfected.hamcrest.dom.DomMatchers.matches;
import static org.testinfected.hamcrest.dom.DomMatchers.matchesInAnyOrder;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.validVisaDetails;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class OrderPageTest {

    String ORDER_TEMPLATE = "order";

    Element orderPage;
    OrderBuilder order = anOrder().withNumber("00000100");

    @Test public void
    displaysOrderSummary() {
        order.from(aCart().containing(anItem().priced("110.00"), anItem().priced("140.00")));
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasOrderNumber("00000100"));
        assertThat("order page", orderPage, hasOrderTotal("250.00"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysOrderDetailsColumnHeadings() {
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage,
                hasSelector("table#order-details tr th",
                        matches(hasText("Quantity"),
                                hasText("Item"),
                                hasText("Price"),
                                hasText("Total"))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysLineItemsInColumns() {
        ItemBuilder anItem = anItem().withNumber("12345678").describedAs("Green Adult").priced("100.00");
        order.from(aCart().containing(anItem, anItem));
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasSelector("#order-details tr#line-item-12345678 td",
                matches(hasText("2"),
                        hasText(containsString("Green Adult")),
                        hasText("100.00"),
                        hasText("200.00"))));
    }

    @Test public void
    displaysOneLineItemPerRow() {
        order.from(aCart().containing(anItem().withNumber("12345678"), anItem().withNumber("12345678"), anItem().withNumber("87654321")));
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasSelector("#order-details tr[id^='line-item']", hasSize(2)));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysBillingInformation() {
        order.paidWith(validVisaDetails().but().billedTo(anAddress().withFirstName("John").withLastName("Doe").withEmail("jdoe@gmail.com")));
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasSelector("#billing-address span", matchesInAnyOrder(
                hasText("John"),
                hasText("Doe"),
                hasText("jdoe@gmail.com"))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    displaysPaymentDetails() {
        order.paidWith(aVisa().withNumber("9999 9999 9999").withExpiryDate("12/12"));
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasSelector("#payment-details span", matchesInAnyOrder(
                hasText("Visa"),
                hasText("9999 9999 9999"),
                hasText("12/12"))));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        orderPage = renderOrderPage().asDom();
        assertThat("order page", orderPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    private Matcher<Element> hasOrderNumber(final String orderNumber) {
        return hasUniqueSelector("span#order-number", hasText(orderNumber));
    }

    private Matcher<Element> hasOrderTotal(final String orderTotal) {
        return hasUniqueSelector("table tr td#order-total", hasText(orderTotal));
    }

    private OfflineRenderer renderOrderPage() {
        return render(ORDER_TEMPLATE).from(WebRoot.pages()).with("order", order);
    }
}
