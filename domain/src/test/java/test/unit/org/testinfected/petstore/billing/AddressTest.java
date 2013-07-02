package test.unit.org.testinfected.petstore.billing;

import org.hamcrest.CoreMatchers;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.ConstraintViolation;
import org.testinfected.petstore.Validator;
import org.testinfected.petstore.billing.Address;
import test.support.org.testinfected.petstore.builders.Builder;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.aValidAddress;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;

public class AddressTest {

    Validator validator = new Validator();
    String MISSING = null;

    @Test public void
    addressesMatchWhenAllPropertiesMatch() {
        Address address = anAddress().
                withFirstName("John").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        Address shouldMatch = anAddress().
                withFirstName("John").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        Address shouldNotMatch = anAddress().
                withFirstName("Jane").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        assertThat("address", address, equalTo(shouldMatch));
        assertThat("hash code", address.hashCode(), equalTo(shouldMatch.hashCode()));
        assertThat("address ", address, not(equalTo(shouldNotMatch)));
    }

    @Test public void
    isInvalidWithoutAFirstName() {
        assertThat("validation of address with missing first name", validationOf(anAddress().withFirstName(MISSING)), violates(on("firstName"), withMessage("missing")));
    }

    @Test public void
    isInvalidWithoutALastName() {
        assertThat("validation of address with missing last name", validationOf(anAddress().withLastName(MISSING)), violates(on("lastName"), withMessage("missing")));
    }

    @Test public void
    isValidWithAFullName() {
        assertThat("validation of valid address", validationOf(aValidAddress()), succeeds());
    }

    private Matcher<Iterable<?>> succeeds() {
        return Matchers.describedAs("succeeds", emptyIterable());
    }

    private <T> Set<ConstraintViolation<?>> validationOf(Builder<T> builder) {
        return validator.validate(builder.build());
    }

    private Matcher<Iterable<? super ConstraintViolation<?>>> violates(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.hasItem(violation(pathMatcher, messageMatcher));
    }

    private Matcher<ConstraintViolation<?>> violation(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.allOf(pathMatcher, messageMatcher);
    }


    public static Matcher<ConstraintViolation<?>> on(String path) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(CoreMatchers.equalTo(path), "on path", "path") {
            @Override protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.path();
            }
        };
    }

    public static Matcher<ConstraintViolation<?>> withMessage(String message) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(equalTo(message), "with message", "message") {
            @Override protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.message();
            }
        };
    }
}
