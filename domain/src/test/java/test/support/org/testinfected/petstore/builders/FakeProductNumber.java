package test.support.org.testinfected.petstore.builders;

public class FakeProductNumber {

    private static final int MAX_PRODUCT_NUMBER = 10000;
    private final RandomNumber randomGenerator = new RandomNumber(MAX_PRODUCT_NUMBER);

    public static String aProductNumber() {
        return new FakeProductNumber().generate();
    }

    public String generate() {
        return "XXX-" + randomGenerator.generate();
    }
}
