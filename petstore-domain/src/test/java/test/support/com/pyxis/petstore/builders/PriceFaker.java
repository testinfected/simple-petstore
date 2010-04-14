package test.support.com.pyxis.petstore.builders;

import org.apache.commons.lang.StringUtils;

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
        return StringUtils.leftPad(decimalPartGenerator.generateNumber(), 2, "0");
    }

    private String integerPart() {
        return integerPartGenerator.generateNumber();
    }
}
