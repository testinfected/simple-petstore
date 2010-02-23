package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.DescribedAs;
import org.w3c.dom.Element;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.DescribedAs.describedAs;

public class HasAttribute extends FeatureMatcher<Element, String> {
    private final String attributeName;

    public HasAttribute(String attributeName, Matcher<? super String> attributeValueMatcher) {
        super(attributeValueMatcher, "an element with attribute \"" + attributeName + "\"", "element attribute \"" + attributeName + "\"");
        this.attributeName = attributeName;
    }

    @Override
    protected String featureValueOf(Element actual) {
        return actual.getAttribute(attributeName);
    }

    @Factory
    public static Matcher<? super Element> hasAttribute(String name, Matcher<? super String> valueMatcher) {
        return new HasAttribute(name, valueMatcher);
    }

    @Factory
    public static Matcher<? super Element> hasAttribute(String name, String value) {
        return hasAttribute(name, equalTo(value));
    }

    @Factory
    public static Matcher<? super Element> withId(String id) {
        return hasAttribute("id", equalTo(id));
    }

    @Factory
    public static Matcher<? super Element> withClassName(String className) {
        return hasAttribute("class", containsString(className));
    }
}
