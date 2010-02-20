package test.integration.com.pyxis.petstore.persistence.support;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static test.integration.com.pyxis.petstore.persistence.support.HasFieldWithValue.fieldValueOf;

public class SamePersistentFieldsAs<T> extends TypeSafeMatcher<T> {

    private final T expectedEntity;

    public SamePersistentFieldsAs(T expectedEntity) {
        this.expectedEntity = expectedEntity;
    }

    public boolean matchesSafely(T argument) {
        return allOf(persistentFieldsValuesOf(expectedEntity)).matches(argument);
    }

    private static <T> Iterable<Matcher<? extends T>> persistentFieldsValuesOf(T entity) {
        Collection<Matcher<? extends T>> valueMatchers = new ArrayList<Matcher<? extends T>>();

        for (Field field : persistentFieldsOf(entity)) {
            valueMatchers.add(HasFieldWithValue.<T>hasField(nameOf(field), equalTo(fieldValueOf(entity, field))));
        }
        return valueMatchers;
    }

    private static String nameOf(Field field) {
        return field.getName();
    }

    private static <T> Field[] persistentFieldsOf(T entity) {
        return entity.getClass().getDeclaredFields();
    }

    public void describeTo(Description description) {
        description.appendText("an argument with persistent fields {");
        boolean addSeparator = false;
        for (Field field : persistentFieldsOf(expectedEntity)) {
            if (addSeparator) description.appendText(", ");
            description.appendText(field.getName());
            description.appendText(": ");
            description.appendValue(fieldValueOf(expectedEntity, field));
            addSeparator = true;
        }
        description.appendText("}");
    }

    @Factory
    public static <T> Matcher<T> samePersistentFieldsAs(T entity) {
        return new SamePersistentFieldsAs<T>(entity);
    }
}