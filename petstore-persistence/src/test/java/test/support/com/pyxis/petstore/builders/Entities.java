package test.support.com.pyxis.petstore.builders;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    private Entities() {}

    public static <T> List<T> entities(EntityBuilder<T>... entityBuilders) {
        List<T> entities = new ArrayList<T>();
        for (EntityBuilder<T> entityBuilder : entityBuilders) {
            entities.add(entityBuilder.build());
        }
        return entities;
    }
}
