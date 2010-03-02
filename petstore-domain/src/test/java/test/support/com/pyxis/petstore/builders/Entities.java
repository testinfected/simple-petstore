package test.support.com.pyxis.petstore.builders;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    private Entities() {}

    @SuppressWarnings("unchecked")
	public static List entities(EntityBuilder... entityBuilders) {
        List entities = new ArrayList();
        for (EntityBuilder entityBuilder : entityBuilders) {
            entities.add(entityBuilder.build());
        }
        return entities;
    }
}
