package test.support.com.pyxis.petstore.builders;

public class ProductNumberFaker {

    private static final int MAX_PRODUCT_NUMBER = 10000;
    private final RandomNumberGenerator randomGenerator = new RandomNumberGenerator(MAX_PRODUCT_NUMBER);

    public static String aProductNumber() {
        return new ProductNumberFaker().fakeProductNumber();
    }

    public String fakeProductNumber() {
        return "XXX-" + randomGenerator.generateNumber();
    }
}
