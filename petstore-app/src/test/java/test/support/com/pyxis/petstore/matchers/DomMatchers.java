package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.w3c.dom.Element;

import static org.hamcrest.Matchers.equalTo;

public class DomMatchers {

    private DomMatchers() {}

    public static Matcher<? super Element> hasSelector(String selector, Matcher<Iterable<? super Element>> elementsMatcher) {
        return HasSelector.hasSelector(selector, elementsMatcher);
    }

    public static Matcher<? super Element> hasUniqueSelector(String selector, Matcher<? super Element> elementMatcher) {
        return HasUniqueSelector.hasUniqueSelector(selector, elementMatcher);
    }

    public static Matcher<Iterable<? super Element>> hasElement(final Matcher<? super Element> elementMatcher) {
        return Matchers.hasItem(elementMatcher);
    }

    public static Matcher<? super Element> withTag(String tagName) {
        return WithTag.withTag(equalTo(tagName));
    }

    public static Matcher<? super Element> withText(String contentText) {
        return WithContentText.withText(equalTo(contentText));
    }

    public static Matcher<? super Element> withText(Matcher<? super String> contentMatcher) {
        return WithContentText.withText(contentMatcher);
    }

    public static Matcher<? super Element> hasAttribute(String name, Matcher<? super String> valueMatcher) {
        return HasAttribute.hasAttribute(name, valueMatcher);
    }

    public static Matcher<? super Element> hasAttribute(String name, String value) {
        return HasAttribute.hasAttribute(name, equalTo(value));
    }

    public static Matcher<? super Element> withId(String id) {
        return HasAttribute.withId(id);
    }

    public static Matcher<? super Element> withClassName(String className) {
        return HasAttribute.withClassName(className);
    }

    public static Matcher<? super Element> hasChildren(Matcher<Iterable<? super Element>> childrenMatcher) {
        return HasChildElements.hasChildren(childrenMatcher);
    }
}
