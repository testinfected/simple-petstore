package test.com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.order.OrderNumber;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.pyxis.matchers.validation.ViolationMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.com.pyxis.petstore.builders.OrderNumberFaker.aNumber;
import static test.support.com.pyxis.petstore.validation.ValidationOf.validationOf;

public class OrderNumberTest {

    String SHOULD_NOT_BE_NULL = "{javax.validation.constraints.NotNull.message}";

    @Test public void
    isValidWithANumber() {
        OrderNumber aValidOrderNumber = aNumber();
        assertThat("constraint violations", validationOf(aValidOrderNumber), succeeds());
    }

    @Test public void
    isInvalidWithoutANumber() {
        OrderNumber anEmptyOrderNumber = new OrderNumber(null);
        assertThat("constraint violations", validationOf(anEmptyOrderNumber), violates(on("number"), withError(SHOULD_NOT_BE_NULL)));
    }
    
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
        OrderNumber orderNumber = new OrderNumber(1);
        assertThat("number", orderNumber.getNumber(), equalTo("00000001"));
    }
}