package test.support.org.testinfected.petstore.jdbc;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.testinfected.petstore.db.FieldAccessor;

import java.lang.reflect.Field;

public class HasFieldWithValue<T, U> extends TypeSafeDiagnosingMatcher<T> {

    private final String fieldName;
    private final Matcher<? super U> valueMatcher;

    public HasFieldWithValue(String fieldName, Matcher<? super U> valueMatcher) {
        this.fieldName = fieldName;
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(T argument, Description mismatchDescription) {
        Field field = FieldAccessor.findField(argument, fieldName);
        if (field == null) {
            mismatchDescription.appendText("no field \"" + fieldName + "\"");
            return false;
        }

        Object fieldValue = new FieldAccessor<>(argument, field).get();
        boolean valueMatches = valueMatcher.matches(fieldValue);
        if (!valueMatches) {
            mismatchDescription.appendText("\"" + fieldName + "\" ");
            valueMatcher.describeMismatch(fieldValue, mismatchDescription);
        }
        return valueMatches;
    }

    public void describeTo(Description description) {
        description.appendText("has field \"");
        description.appendText(fieldName);
        description.appendText("\": ");
        description.appendDescriptionOf(valueMatcher);
    }

    public static <T, U> Matcher<T> hasField(String field, Matcher<? super U> value) {
        return new HasFieldWithValue<T, U>(field, value);
    }
}