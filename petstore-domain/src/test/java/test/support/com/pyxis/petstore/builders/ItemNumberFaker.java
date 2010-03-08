package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.ItemNumber;
import com.pyxis.petstore.domain.ItemNumberGenerator;

public class ItemNumberFaker implements ItemNumberGenerator {

    private static final int MAX_ITEM_NUMBER = 100000000;
    private final RandomNumberGenerator faker = new RandomNumberGenerator(MAX_ITEM_NUMBER);

    public ItemNumber nextItemNumber() {
        return new ItemNumber(faker.generateNumber());
    }

    public static ItemNumber aNumber() {
        return new ItemNumberFaker().nextItemNumber();
    }
}
