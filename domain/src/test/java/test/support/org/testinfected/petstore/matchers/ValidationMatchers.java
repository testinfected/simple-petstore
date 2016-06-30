package test.support.org.testinfected.petstore.matchers;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testinfected.petstore.validation.ConstraintViolation;
import org.testinfected.petstore.validation.Validator;
import test.support.org.testinfected.petstore.builders.Builder;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyIterable;

public class ValidationMatchers {

    public static Set<ConstraintViolation<?>> validationOf(Builder<?> builder) {
        return validationOf(builder.build());
    }

    public static Set<ConstraintViolation<?>> validationOf(Object target) {
        Validator validator = new Validator();
        return validator.validate(target);
    }

    public static Matcher<Iterable<?>> succeeds() {
        return Matchers.describedAs("succeeds", emptyIterable());
    }

    public static Matcher<Iterable<?>> fails() {
        return Matchers.describedAs("fails", not(emptyIterable()));
    }

    public static Matcher<Iterable<? super ConstraintViolation<?>>> violates(Matcher<ConstraintViolation<?>> pathMatcher) {
        return Matchers.hasItem(pathMatcher);
    }

    public static Matcher<Iterable<? super ConstraintViolation<?>>> violates(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.hasItem(violation(pathMatcher, messageMatcher));
    }

    public static Matcher<Iterable<? super ConstraintViolation<?>>> violates(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher, Matcher<ConstraintViolation<?>> valueMatcher) {
        return Matchers.hasItem(violation(pathMatcher, messageMatcher, valueMatcher));
    }

    private static Matcher<ConstraintViolation<?>> violation(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher) {
        return Matchers.allOf(pathMatcher, messageMatcher);
    }

    private static Matcher<ConstraintViolation<?>> violation(Matcher<ConstraintViolation<?>> pathMatcher, Matcher<ConstraintViolation<?>> messageMatcher, Matcher<ConstraintViolation<?>> valueMatcher) {
        return Matchers.allOf(pathMatcher, messageMatcher, valueMatcher);
    }

    public static Matcher<ConstraintViolation<?>> on(String path) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(equalTo(path), "on path", "path") {
            protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.path();
            }
        };
    }

    public static Matcher<ConstraintViolation<?>> withMessage(String message) {
        return new FeatureMatcher<ConstraintViolation<?>, String>(equalTo(message), "with message", "message") {
            protected String featureValueOf(ConstraintViolation<?> actual) {
                return actual.error();
            }
        };
    }

    public static Matcher<ConstraintViolation<?>> withValue(Object value) {
        return new FeatureMatcher<ConstraintViolation<?>, Object>(equalTo(value), "with value", "value") {
            protected Object featureValueOf(ConstraintViolation<?> actual) {
                return actual.value();
            }
        };
    }

    private ValidationMatchers() {}
}
