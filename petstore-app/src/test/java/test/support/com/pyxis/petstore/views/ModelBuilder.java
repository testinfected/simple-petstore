package test.support.com.pyxis.petstore.views;

import org.springframework.ui.ModelMap;
import test.support.com.pyxis.petstore.builders.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelBuilder {

    private ModelMap attributes = new ModelMap();

    public static Map<String, Object> anEmptyModel() {
        return aModel().asMap();
    }

    public static ModelBuilder aModel() {
        return new ModelBuilder();
    }

    public ModelBuilder listing(Builder<?>... builders) {
        List<Object> list = new ArrayList<Object>();
        for (Builder builder : builders) {
            list.add(builder.build());
        }
        return with(list);
    }

    public ModelBuilder with(Builder<?> builder) {
        return with(builder.build());
    }

    public ModelBuilder with(Object attribute) {
        attributes.addAttribute(attribute);
        return this;
    }

    public ModelBuilder with(String name, Object attribute) {
        attributes.addAttribute(name, attribute);
        return this;
    }

    public Map<String, Object> asMap() {
        return new ModelMap().addAllAttributes(attributes);
    }
}
