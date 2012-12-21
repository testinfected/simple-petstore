package test.support.org.testinfected.petstore.builders;

import java.util.Random;

import static java.lang.String.valueOf;

public class RandomNumber {

    private final int maxValue;
    private final Random random;

    public RandomNumber(int maxValue) {
        this.maxValue = maxValue;
        random = new Random();
    }

    public String generate() {
        return valueOf(random.nextInt(maxValue));
    }
}
