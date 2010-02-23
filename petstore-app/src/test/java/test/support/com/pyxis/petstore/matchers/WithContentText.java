package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.w3c.dom.Element;

import static org.hamcrest.Matchers.equalTo;

public class WithContentText extends FeatureMatcher<Element, String> {

    public WithContentText(Matcher<? super String> contentMatcher) {
        super(contentMatcher, "an element with content text", "element content text");
    }

    @Override
    protected String featureValueOf(Element actual) {
        return actual.getTextContent();
    }

    @Factory
    public static Matcher<? super Element> withText(Matcher<? super String> contentMatcher) {
        return new WithContentText(contentMatcher);
    }

    @Factory
    public static Matcher<? super Element> withText(String contentText) {
        return withText(equalTo(contentText));
    }
}
