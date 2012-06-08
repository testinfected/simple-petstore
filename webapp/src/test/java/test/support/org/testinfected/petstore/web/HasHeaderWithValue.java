package test.support.org.testinfected.petstore.web;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class HasHeaderWithValue extends FeatureMatcher<WebResponse, String> {

    private final String name;

    public HasHeaderWithValue(String name, Matcher<? super String> subMatcher) {
        super(subMatcher, "has header '" + name + "' with value", "header '" + name + "'");
        this.name = name;
    }

    protected String featureValueOf(WebResponse actual) {
        return actual.getResponseHeaderValue(name);
    }

    public static Matcher<? super WebResponse> hasHeader(final String name, final Matcher<? super String> valueMatcher) {
        return new HasHeaderWithValue(name, valueMatcher);
    }
}
