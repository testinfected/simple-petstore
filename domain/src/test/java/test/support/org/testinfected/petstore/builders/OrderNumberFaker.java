package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.order.OrderNumber;
import org.testinfected.petstore.order.OrderNumberSequence;

public class OrderNumberFaker implements OrderNumberSequence {

    private static final int MAX_ORDER_NUMBER = 100000000;
    private final RandomNumberGenerator faker = new RandomNumberGenerator(MAX_ORDER_NUMBER);

    public static OrderNumber aNumber() {
        return new OrderNumberFaker().nextOrderNumber();
    }

    public OrderNumber nextOrderNumber() {
        return new OrderNumber(faker.generateNumber());
    }
}
