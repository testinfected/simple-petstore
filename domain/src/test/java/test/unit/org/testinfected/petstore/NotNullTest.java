package test.unit.org.testinfected.petstore;

import org.junit.Test;
import org.testinfected.petstore.ConstraintViolationException;
import org.testinfected.petstore.NotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.on;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.violates;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withMessage;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withValue;

public class NotNullTest {

    static final Object NULL = null;
    static final String ROOT = null;

    @Test public void
    guardsAgainstAccessToNullValue() {
        NotNull<?> notNull = new NotNull<Object>(NULL);
        try {
            notNull.get();
            fail("Expected " + ConstraintViolationException.class);
        } catch (ConstraintViolationException expected) {
            assertThat("violations", expected.violations(), violates(on(ROOT), withMessage("missing"), withValue(NULL)));
        }
    }
}
