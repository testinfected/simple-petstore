package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.product.ItemNumber;

public class FakeItemNumber {

    private static final int MAX_ITEM_NUMBER = 100000000;
    private final RandomNumber faker = new RandomNumber(MAX_ITEM_NUMBER);

    public ItemNumber generate() {
        return new ItemNumber(faker.generate());
    }

    public static ItemNumber anItemNumber() {
        return new FakeItemNumber().generate();
    }
}
