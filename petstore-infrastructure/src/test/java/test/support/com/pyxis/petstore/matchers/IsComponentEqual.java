package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.nullValue;
import static test.support.com.pyxis.petstore.matchers.SamePersistentFieldsAs.persistentFieldsOf;

public class IsComponentEqual<T> extends DiagnosingMatcher<T> {

    private final T component;

    public IsComponentEqual(T expectedComponent) {
        this.component = expectedComponent;
    }

    protected boolean matches(Object argument, Description mismatchDescription) {
        if (component == null && argument == null) return true;

        if (component == null) {
            Matcher<T> valueMatcher = allNullFields(argument);
            boolean match = valueMatcher.matches(argument);
            if (!match) valueMatcher.describeMismatch(argument, mismatchDescription);
            return match;
        }

        if (argument == null) {
            Matcher<T> valueMatcher = allNullFields(component);
            boolean match = valueMatcher.matches(component);
            if (!match) mismatchDescription.appendText("is null");
            return match;
        }

        Matcher<T> valueMatcher = new SamePersistentFieldsAs<T>(component);
        boolean match = valueMatcher.matches(argument);
        if (!match) valueMatcher.describeMismatch(argument, mismatchDescription);
        return match;
    }

    private Matcher<T> allNullFields(final Object target) {
        Collection<Matcher<? super T>> nullFields = new ArrayList<Matcher<? super T>>();
        for (Field field : persistentFieldsOf(target.getClass())) {
            nullFields.add(new HasFieldWithValue<T>(field.getName(), nullValue()));
        }
        return allOf(nullFields);
    }
    
    public void describeTo(Description description) {
        if (component == null) {
            description.appendText("null");
        } else {
            new SamePersistentFieldsAs<T>(component).describeTo(description);
        }
    }

    @Factory
    public static <T> Matcher<T> componentEqualTo(T component) {
        return new IsComponentEqual<T>(component);
    }
}
