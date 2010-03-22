package test.support.com.pyxis.petstore.builders;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    private Entities() {}

	public static List<?> entities(Builder<?>... builders) {
        List<Object> entities = new ArrayList<Object>();
        for (Builder builder : builders) {
            entities.add(builder.build());
        }
        return entities;
    }
}
