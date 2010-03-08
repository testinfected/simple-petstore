package test.support.com.pyxis.petstore.builders;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    private Entities() {}

	public static List<?> entities(EntityBuilder<?>... entityBuilders) {
        List<Object> entities = new ArrayList<Object>();
        for (EntityBuilder entityBuilder : entityBuilders) {
            entities.add(entityBuilder.build());
        }
        return entities;
    }
}
