package test.support.org.testinfected.petstore.builders;

import java.math.BigDecimal;

public class FakePrice {

    private static final BigDecimal DOLLAR_IN_CENTS = new BigDecimal(100);
    private final RandomNumber generator = new RandomNumber(100000);

    public static BigDecimal aPrice() {
        return new FakePrice().generate();
    }

    public BigDecimal generate() {
        BigDecimal cents = new BigDecimal(random());
        return dollars(cents);
    }

    private BigDecimal dollars(BigDecimal cents) {
        return cents.divide(DOLLAR_IN_CENTS).setScale(2);
    }

    private String random() {
        return generator.generate();
    }
}
