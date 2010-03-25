package test.support.com.pyxis.petstore.builders;

import java.math.BigDecimal;

public class PriceFaker {

    private final RandomNumberGenerator integerPartGenerator;
    private final RandomNumberGenerator decimalPartGenerator;

    public static BigDecimal aPrice() {
        return new PriceFaker().fakePrice();
    }

    public PriceFaker() {
        integerPartGenerator = new RandomNumberGenerator(1000);
        decimalPartGenerator = new RandomNumberGenerator(100);
    }

    public BigDecimal fakePrice() {
        return new BigDecimal(integerPart() + "." + decimalPart());
    }

    private String decimalPart() {
        return decimalPartGenerator.generateNumber();
    }

    private String integerPart() {
        return integerPartGenerator.generateNumber();
    }
}
