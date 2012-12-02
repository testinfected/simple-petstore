package test.support.org.testinfected.support.web;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;

public class HasHeaderWithValue extends FeatureMatcher<WebResponse, String> {

    private final String name;

    public HasHeaderWithValue(String name, Matcher<? super String> subMatcher) {
        super(subMatcher, "has header '" + name + "' with value", "header '" + name + "'");
        this.name = name;
    }

    protected String featureValueOf(WebResponse actual) {
        return actual.getResponseHeaderValue(name);
    }

    public static Matcher<? super WebResponse> hasNoHeader(String name) {
        return hasHeader(name, nullValue());
    }

    public static Matcher<? super WebResponse> hasHeader(String name, String value) {
        return hasHeader(name, equalTo(value));
    }

    public static Matcher<? super WebResponse> hasHeader(String name, Matcher<? super String> valueMatcher) {
        return new HasHeaderWithValue(name, valueMatcher);
    }
}
