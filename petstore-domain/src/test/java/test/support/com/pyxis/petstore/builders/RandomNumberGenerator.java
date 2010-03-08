package test.support.com.pyxis.petstore.builders;

import java.util.Random;

import static java.lang.String.valueOf;

public class RandomNumberGenerator {

    private final int maxValue;
    private final Random random;

    public RandomNumberGenerator(int maxValue) {
        this.maxValue = maxValue;
        random = new Random();
    }

    public String generateNumber() {
        return valueOf(random.nextInt(maxValue));
    }
}
