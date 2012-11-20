package test.support.org.testinfected.petstore.builders;

import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.ItemNumberSequence;

public class ItemNumberFaker implements ItemNumberSequence {

    private static final int MAX_ITEM_NUMBER = 100000000;
    private final RandomNumberGenerator faker = new RandomNumberGenerator(MAX_ITEM_NUMBER);

    public ItemNumber nextItemNumber() {
        return new ItemNumber(faker.generateNumber());
    }

    public static ItemNumber aNumber() {
        return new ItemNumberFaker().nextItemNumber();
    }
}
