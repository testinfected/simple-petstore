package test.integration.com.pyxis.petstore.persistence.support;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.AllOf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static test.integration.com.pyxis.petstore.persistence.support.HasFieldWithValue.fieldValueOf;

public class SamePersistentFieldsAs<T> extends TypeSafeDiagnosingMatcher<T> {

    private final T expectedEntity;

    public SamePersistentFieldsAs(T expectedEntity) {
        this.expectedEntity = expectedEntity;
    }

    @Override
    protected boolean matchesSafely(T argument, Description mismatchDescription) {
        return new AllOf<T>(persistentFieldsValuesOf(expectedEntity)).matches(argument, mismatchDescription);
    }

    private static <T> Iterable<Matcher<? super T>> persistentFieldsValuesOf(T entity) {
        Collection<Matcher<? super T>> valueMatchers = new ArrayList<Matcher<? super T>>();

        for (Field field : persistentFieldsOf(entity)) {
            valueMatchers.add(new HasFieldWithValue<T>(nameOf(field), equalTo(fieldValueOf(entity, field))));
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
        description.appendText("with persistent fields ");
        boolean addSeparator = false;
        for (Field field : persistentFieldsOf(expectedEntity)) {
            if (addSeparator) description.appendText(", ");
            description.appendText("\"" + field.getName() + "\" ");
            description.appendValue(fieldValueOf(expectedEntity, field));
            addSeparator = true;
        }
    }

    @Factory
    public static <T> Matcher<T> samePersistentFieldsAs(T entity) {
        return new SamePersistentFieldsAs<T>(entity);
    }
}