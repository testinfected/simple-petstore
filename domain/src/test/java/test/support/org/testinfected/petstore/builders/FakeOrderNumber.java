package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.order.OrderNumber;

public class FakeOrderNumber {

    private static final int MAX_ORDER_NUMBER = 100000000;
    private final RandomNumber faker = new RandomNumber(MAX_ORDER_NUMBER);

    public static OrderNumber anOrderNumber() {
        return new FakeOrderNumber().generate();
    }

    public OrderNumber generate() {
        return new OrderNumber(faker.generate());
    }
}
