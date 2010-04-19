package test.support.com.pyxis.petstore.builders;

import java.math.BigDecimal;

public class PriceFaker {

    private static final BigDecimal DOLLAR_IN_CENTS = new BigDecimal(100);
    private final RandomNumberGenerator generator;

    public static BigDecimal aPrice() {
        return new PriceFaker().fakePrice();
    }

    public PriceFaker() {
        generator = new RandomNumberGenerator(100000);
    }

    public BigDecimal fakePrice() {
        BigDecimal cents = new BigDecimal(random());
        return dollars(cents);
    }

    private BigDecimal dollars(BigDecimal cents) {
        return cents.divide(DOLLAR_IN_CENTS).setScale(2);
    }

    private String random() {
        return generator.generateNumber();
    }
}
