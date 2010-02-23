package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.w3c.dom.Element;

import static org.hamcrest.Matchers.equalTo;

public class WithTag extends FeatureMatcher<Element, String> {

    public WithTag(Matcher<? super String> valueMatcher) {
        super(valueMatcher, "an element with tag", "element tag");
    }

    @Override
    protected String featureValueOf(Element actual) {
        return actual.getTagName();
    }

    public static Matcher<? super Element> withTag(Matcher<? super String> valueMatcher) {
        return new WithTag(valueMatcher);
    }

    @Factory
    public static Matcher<? super Element> withTag(String tagName) {
        return withTag(equalTo(tagName));
    }
}
