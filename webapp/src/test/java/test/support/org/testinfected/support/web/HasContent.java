package test.support.org.testinfected.support.web;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class HasContent extends FeatureMatcher<WebResponse, String> {

    public HasContent(Matcher<? super String> subMatcher) {
        super(subMatcher, "has content", "content");
    }

    protected String featureValueOf(WebResponse actual) {
        return actual.getContentAsString();
    }

    public static Matcher<? super WebResponse> hasContent(Matcher<? super String> contentMatcher) {
        return new HasContent(contentMatcher);
    }
}
