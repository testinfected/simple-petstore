package test.support.org.testinfected.molecule.web;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class HasStatusCode extends FeatureMatcher<WebResponse, Integer> {

    public HasStatusCode(Matcher<? super Integer> subMatcher) {
        super(subMatcher, "has status code", "status code");
    }

    protected Integer featureValueOf(WebResponse actual) {
        return actual.getStatusCode();
    }

    public static Matcher<? super WebResponse> hasStatusCode(int code) {
        return hasStatusCode(equalTo(code));
    }
    
    public static Matcher<? super WebResponse> hasStatusCode(Matcher<? super Integer> codeMatcher) {
        return new HasStatusCode(codeMatcher);
    }
}
