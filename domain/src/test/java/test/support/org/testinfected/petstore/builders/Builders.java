package test.support.org.testinfected.petstore.builders;

import java.util.ArrayList;
import java.util.List;

// todo see where it can be used to eliminate duplication
public class Builders {

    public static <T> List<T> build(Builder<T>... builders) {
        final List<T> result = new ArrayList<T>();
        for (Builder<T> builder : builders) {
            result.add(builder.build());
        }
        return result;
    }
}
