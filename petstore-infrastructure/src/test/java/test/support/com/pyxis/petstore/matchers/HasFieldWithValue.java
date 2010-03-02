package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.pyxis.petstore.ExceptionImposter;

import java.lang.reflect.Field;

public class HasFieldWithValue<T> extends TypeSafeDiagnosingMatcher<T> {

    private final String fieldName;
    private final Matcher valueMatcher;

    public HasFieldWithValue(String fieldName, Matcher valueMatcher) {
        this.fieldName = fieldName;
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(T argument, Description mismatchDescription) {
        Field field = getField(argument, mismatchDescription);
        if (field == null) return false;

        Object fieldValue = fieldValueOf(argument, field);
        boolean valueMatches = valueMatcher.matches(fieldValue);
        if (!valueMatches) {
            mismatchDescription.appendText("field \"" + fieldName + "\" ");
            valueMatcher.describeMismatch(fieldValue, mismatchDescription);
        }
        return valueMatches;
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

    private Field getField(Object argument, Description mismatchDescription) {
        try {
            return argument.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            mismatchDescription.appendText("no field \"" + fieldName + "\"");
            return null;
        }
    }

    public void describeTo(Description description) {
        description.appendText("has field \"");
        description.appendText(fieldName);
        description.appendText("\" ");
        description.appendDescriptionOf(valueMatcher);
    }

    @Factory
    public static <T> Matcher<T> hasField(String field, Matcher value) {
        return new HasFieldWithValue<T>(field, value);
    }
}
