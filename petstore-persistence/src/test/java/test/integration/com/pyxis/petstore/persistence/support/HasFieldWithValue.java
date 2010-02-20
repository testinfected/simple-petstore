package test.integration.com.pyxis.petstore.persistence.support;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;

public class HasFieldWithValue<T> extends TypeSafeMatcher<T> {

    private final String fieldName;
    private final Matcher value;

    public HasFieldWithValue(String fieldName, Matcher value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public boolean matchesSafely(T argument) {
        try {
            Field field = getField(argument);
            return field != null && value.matches(fieldValueOf(argument, field));
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IntrospectionException e) {
            return false;
        }                                       
    }

    public static Object fieldValueOf(Object argument, Field field) {
        byPassSecurity(field);
        try {
            return field.get(argument);
        } catch (IllegalAccessException e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    private static void byPassSecurity(Field field) {
        field.setAccessible(true);
    }
            
    private Field getField(Object argument) throws IntrospectionException, NoSuchFieldException {
        return argument.getClass().getDeclaredField(fieldName);
    }

    public void describeTo(Description description) {
        description.appendText("an argument with ");
        description.appendText(fieldName);
        description.appendText(": ");
        description.appendDescriptionOf(value);
    }

    @Factory
    public static <T> Matcher<T> hasField(String field, Matcher value) {
        return new HasFieldWithValue<T>(field, value);
    }
}
