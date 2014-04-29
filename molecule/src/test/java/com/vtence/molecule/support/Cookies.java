package com.vtence.molecule.support;

import com.vtence.molecule.http.Cookie;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class Cookies {

    private Cookies() {}

    public static Matcher<? super Cookie> cookieWithValue(String value) {
        return cookieWithValue(equalTo(value));
    }

    public static Matcher<? super Cookie> cookieWithValue(final Matcher<? super String> matching) {
        return new FeatureMatcher<Cookie, String>(matching, "cookie with value", "value") {
            protected String featureValueOf(Cookie actual) {
                return actual.value();
            }
        };
    }

    public static Matcher<? super Cookie> httpOnlyCookie(boolean httpOnly) {
        return httpOnlyCookie(is(httpOnly));
    }

    public static Matcher<? super Cookie> httpOnlyCookie(final Matcher<? super Boolean> matching) {
        return new FeatureMatcher<Cookie, Boolean>(matching, "http only cookie", "http only") {
            protected Boolean featureValueOf(Cookie actual) {
                return actual.httpOnly();
            }
        };
    }

    public static Matcher<? super Cookie> cookieWithMaxAge(int maxAge) {
        return cookieWithMaxAge(equalTo(maxAge));
    }

    public static Matcher<? super Cookie> cookieWithMaxAge(final Matcher<? super Integer> matching) {
        return new FeatureMatcher<Cookie, Integer>(matching, "cookie with max age", "max age") {
            protected Integer featureValueOf(Cookie actual) {
                return actual.maxAge();
            }
        };
    }
}
