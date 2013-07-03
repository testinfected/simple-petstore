package test.unit.org.testinfected.petstore;

import org.junit.Test;
import org.testinfected.petstore.ConstraintViolationException;
import org.testinfected.petstore.NotBlank;
import org.testinfected.petstore.NotNull;
import org.testinfected.petstore.Valid;
import org.testinfected.petstore.Validate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.on;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.violates;

public class ValidTest {

    static final String NULL = null;
    static final String BLANK = "   ";
    static final Object INVALID = new Object() {
        NotNull<String> notNull = Validate.notNull(NULL);
        NotBlank notBlank = Validate.notBlank(BLANK);
    };

    @Test public void
    guardsAgainstAccessToInvalidValue() {
        Valid<?> valid = new Valid<Object>(INVALID);
        try {
            valid.get();
            fail("Expected " + ConstraintViolationException.class);
        } catch (ConstraintViolationException expected) {
            assertThat("violations", expected.violations(), allOf(violates(on("notNull")), violates(on("notBlank"))));
        }
    }
}
