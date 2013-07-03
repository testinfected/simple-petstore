package test.unit.org.testinfected.petstore;

import org.junit.Test;
import org.testinfected.petstore.ConstraintViolationException;
import org.testinfected.petstore.NotBlank;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.on;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.violates;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withMessage;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withValue;

public class NotBlankTest {

    static final String BLANK = "  ";
    static final String ROOT = null;

    @Test public void
    guardsAgainstAccessToBlankText() {
        NotBlank notBlank = new NotBlank(BLANK);
        try {
            notBlank.get();
            fail("Expected " + ConstraintViolationException.class);
        } catch (ConstraintViolationException expected) {
            assertThat("violations", expected.violations(), violates(on(ROOT), withMessage("blank"), withValue(BLANK)));
        }
    }
}
