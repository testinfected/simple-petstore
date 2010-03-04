package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.AllOf;

import javax.persistence.Embeddable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.matchers.HasFieldWithValue.fieldValueOf;
import static test.support.com.pyxis.petstore.matchers.HasFieldWithValue.hasField;
import static test.support.com.pyxis.petstore.matchers.IsComponentEqual.componentEqualTo;

public class SamePersistentFieldsAs<T> extends TypeSafeDiagnosingMatcher<T> {

    private final T expectedEntity;

    public SamePersistentFieldsAs(T expectedEntity) {
        this.expectedEntity = expectedEntity;
    }

    @Override
    protected boolean matchesSafely(T argument, Description mismatchDescription) {
        for (Matcher<? super T> matcher : persistentFieldsValuesOf(expectedEntity)) {
            if (!matcher.matches(argument)) {
                matcher.describeMismatch(argument, mismatchDescription);
              return false;
            }
        }
        return true;
    }

    private static <T> Iterable<Matcher<? super T>> persistentFieldsValuesOf(T entity) {
        Collection<Matcher<? super T>> valueMatchers = new ArrayList<Matcher<? super T>>();

        for (Field basicField : persistentFieldsOf(typeOf(entity))) {
            valueMatchers.add(hasField(nameOf(basicField), equalTo(fieldValueOf(entity, basicField))));
        }
        for (Field embeddedField : embeddedFieldsOf(typeOf(entity))) {
            valueMatchers.add(hasField(nameOf(embeddedField), componentEqualTo(fieldValueOf(entity, embeddedField))));
        }
        return valueMatchers;
    }

    private static <T> Class<?> typeOf(Object entity) {
        return entity.getClass();
    }

    private static String nameOf(Field field) {
        return field.getName();
    }

    public static Field[] persistentFieldsOf(Class<?> entity) {
        Field[] allFields = entity.getDeclaredFields();
        List<Field> persistentFields = new ArrayList<Field>();
        for (Field each : allFields) {
            if (isPersistent(each) && !isEmbedded(each)) persistentFields.add(each);
        }
        return persistentFields.toArray(new Field[persistentFields.size()]);
    }

    private static boolean isPersistent(Field each) {
        return !isStatic(each) && !isTransient(each);
    }

    public static Field[] embeddedFieldsOf(Class<?> entity) {
        Field[] allFields = entity.getDeclaredFields();
        List<Field> embeddedFields = new ArrayList<Field>();
        for (Field each : allFields) {
            if (isPersistent(each) && isEmbedded(each)) embeddedFields.add(each);
        }
        return embeddedFields.toArray(new Field[embeddedFields.size()]);
    }

    private static boolean isEmbedded(Field field) {
        return field.getType().getAnnotation(Embeddable.class) != null;
    }

    private static boolean isTransient(Field each) {
        return Modifier.isTransient(each.getModifiers());
    }

    private static boolean isStatic(Field each) {
        return Modifier.isStatic(each.getModifiers());
    }

    public void describeTo(Description description) {
        description.appendText("has fields [");
        boolean addSeparator = false;
        for (Field field : persistentFieldsOf(typeOf(expectedEntity))) {
            if (addSeparator) description.appendText(", ");
            description.appendText(field.getName() + ": ");
            description.appendValue(fieldValueOf(expectedEntity, field));
            addSeparator = true;
        }
        for (Field field : embeddedFieldsOf(typeOf(expectedEntity))) {
            if (addSeparator) description.appendText(", ");
            description.appendText(field.getName() + ": ");
            componentEqualTo(fieldValueOf(expectedEntity, field)).describeTo(description);
            addSeparator = true;
        }
        description.appendText("]");
    }

    @Factory
    public static <T> Matcher<T> samePersistentFieldsAs(T entity) {
        return new SamePersistentFieldsAs<T>(entity);
    }
}