package test.support.com.pyxis.petstore.matchers;

import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.matchers.WithBlankText.blank;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.w3c.dom.Element;

public class WithContentText extends FeatureMatcher<Element, String> {

    public WithContentText(Matcher<? super String> contentMatcher) {
        super(contentMatcher, "an element with content text", "element content text");
    }

    @Override
    protected String featureValueOf(Element actual) {
        return actual.getTextContent();
    }

    @Factory
    public static Matcher<Element> withText(Matcher<? super String> contentMatcher) {
        return new WithContentText(contentMatcher);
    }

    @Factory
    public static Matcher<Element> withText(String contentText) {
        return withText(equalTo(contentText));
    }

	public static Matcher<Element> withBlankText() {
		return withText(blank());
	}

}
