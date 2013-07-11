package test.unit.org.testinfected.petstore.order;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.order.OrderNumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class OrderNumberTest {

    @Test public void
    isUniquelyIdentifiedByItsValue() {
        OrderNumber orderNumber = new OrderNumber("00000001");
        OrderNumber shouldMatch = new OrderNumber("00000001");
        OrderNumber shouldNotMatch = new OrderNumber("00000002");

        assertThat("order number", orderNumber, equalTo(shouldMatch));
        assertThat("hash code", orderNumber.hashCode(), Matchers.equalTo(shouldMatch.hashCode()));
        assertThat("order number", orderNumber, not(equalTo(shouldNotMatch)));
        assertThat("hash code", orderNumber.hashCode(), not(equalTo(shouldNotMatch.hashCode())));
    }
    
    @Test public void
    normalizesValues() {
        assertThat("number", numberFor(1), equalTo("00000001"));
        assertThat("number", numberFor(10000000), equalTo("10000000"));
    }

    private String numberFor(final long number) {
        return new OrderNumber(number).getNumber();
    }
}