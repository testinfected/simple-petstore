package test.support.com.pyxis.petstore.matchers;

import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.matchers.IsBlankString.isBlank;

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
    public static Matcher<Element> withContent(Matcher<? super String> contentMatcher) {
        return new WithContentText(contentMatcher);
    }

    @Factory
    public static Matcher<Element> withContent(String contentText) {
        return withContent(equalTo(contentText));
    }

	public static Matcher<Element> withBlankContent() {
		return withContent(isBlank());
	}

}
