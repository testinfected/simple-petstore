package test.unit.org.testinfected.petstore.views;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.w3c.dom.Element;
import test.support.org.testinfected.petstore.builders.ItemBuilder;
import test.support.org.testinfected.petstore.builders.OrderBuilder;
import test.support.org.testinfected.petstore.web.OfflineRenderer;
import test.support.org.testinfected.petstore.web.WebRoot;

import static com.vtence.hamcrest.dom.DomMatchers.contains;
import static com.vtence.hamcrest.dom.DomMatchers.containsInAnyOrder;
import static com.vtence.hamcrest.dom.DomMatchers.hasAttribute;
import static com.vtence.hamcrest.dom.DomMatchers.hasSelector;
import static com.vtence.hamcrest.dom.DomMatchers.hasSize;
import static com.vtence.hamcrest.dom.DomMatchers.hasText;
import static com.vtence.hamcrest.dom.DomMatchers.hasUniqueSelector;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;
import static test.support.org.testinfected.petstore.builders.CreditCardBuilder.aVisa;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.OrderBuilder.anOrder;
import static test.support.org.testinfected.petstore.web.OfflineRenderer.render;

public class OrderPageTest {

    String ORDER_TEMPLATE = "order";

    Element orderPage;
    OrderBuilder order = anOrder().withNumber("00000100");

    @Test public void
    displaysOrderSummary() {
        orderPage = renderOrderPage().with(
            order.from(aCart().containing(anItem().priced("110.00"), anItem().priced("140.00")))).
                asDom();
        assertThat("order page", orderPage, hasOrderNumber("00000100"));
        assertThat("order page", orderPage, hasOrderTotal("250.00"));
    }

    @Test public void
    displaysOrderDetailsColumnHeadings() {
        orderPage = renderOrderPage().with(order).asDom();
        assertThat("order page", orderPage,
                hasSelector("table#order-details tr th",
                        contains(hasText("Quantity"),
                                hasText("Item"),
                                hasText("Price"),
                                hasText("Total"))));
    }

    @Test public void
    displaysLineItemsInColumns() {
        ItemBuilder item = anItem().withNumber("12345678").describedAs("Green Adult").priced("100.00");
        orderPage = renderOrderPage().with(order.from(aCart().containing(item, item))).asDom();
        assertThat("order page", orderPage, hasSelector("#order-details tr#line-item-12345678 td",
                contains(hasText("2"),
                        hasText(containsString("Green Adult")),
                        hasText("100.00"),
                        hasText("200.00"))));
    }

    @Test public void
    displaysOneLineItemPerRow() {
        orderPage = renderOrderPage().with(
                order.from(aCart().containing(
                        anItem().withNumber("12345678"),
                        anItem().withNumber("12345678"),
                        anItem().withNumber("87654321")))).
                asDom();
        assertThat("order page", orderPage, hasSelector("#order-details tr[id^='line-item']", hasSize(2)));
    }

    @Test public void
    displaysBillingInformation() {
        orderPage = renderOrderPage().with(
                order.paidWith(aVisa().billedTo(anAddress().
                        withFirstName("John").withLastName("Doe").withEmail("jdoe@gmail.com"))))
                .asDom();
        assertThat("order page", orderPage, hasSelector("#billing-address span", containsInAnyOrder(
                hasText("John"),
                hasText("Doe"),
                hasText("jdoe@gmail.com"))));
    }

    @Test public void
    displaysPaymentDetails() {
        orderPage = renderOrderPage().with(order.paidWith(
                aVisa().withNumber("9999 9999 9999").withExpiryDate("12/12"))).asDom();
        assertThat("order page", orderPage, hasSelector("#payment-details span", containsInAnyOrder(
                hasText("Visa"),
                hasText("9999 9999 9999"),
                hasText("12/12"))));
    }

    @Test public void
    returnsToHomePageToContinueShopping() {
        orderPage = renderOrderPage().with(order).asDom();
        assertThat("order page", orderPage, hasUniqueSelector("a.cancel", hasAttribute("href", "/")));
    }

    private Matcher<Element> hasOrderNumber(final String orderNumber) {
        return hasUniqueSelector("span#order-number", hasText(orderNumber));
    }

    private Matcher<Element> hasOrderTotal(final String orderTotal) {
        return hasUniqueSelector("table tr td#order-total", hasText(orderTotal));
    }

    private OfflineRenderer renderOrderPage() {
        return render(ORDER_TEMPLATE).from(WebRoot.pages());
    }
}
