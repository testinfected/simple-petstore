package test.support.org.testinfected.molecule.integration;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;

public class HasStatusMessage extends FeatureMatcher<WebResponse, String> {

    public HasStatusMessage(Matcher<? super String> subMatcher) {
        super(subMatcher, "has status message", "status message");
    }

    protected String featureValueOf(WebResponse actual) {
        return actual.getStatusMessage();
    }

    public static Matcher<? super WebResponse> hasStatusMessage(String message) {
        return hasStatusMessage(equalTo(message));
    }

    public static Matcher<? super WebResponse> hasStatusMessage(Matcher<? super String> messageMatcher) {
        return new HasStatusMessage(messageMatcher);
    }
}
