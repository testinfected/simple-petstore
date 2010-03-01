package test.support.com.pyxis.petstore.matchers;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.w3c.dom.Element;

public class DomMatchers {

    private DomMatchers() {}

    public static Matcher<Element> hasSelector(String selector, Matcher<Iterable<Element>> elementsMatcher) {
        return HasSelector.hasSelector(selector, elementsMatcher);
    }

    public static Matcher<Element> hasUniqueSelector(String selector, Matcher<Element> elementMatcher) {
        return HasUniqueSelector.hasUniqueSelector(selector, elementMatcher);
    }

    public static Matcher<Iterable<Element>> hasElement(Matcher<? super Element> elementMatcher) {
        return Matchers.hasItems(elementMatcher);
    }

    public static Matcher<Iterable<Element>> hasElements(Matcher<? super Element>... elementMatchers) {
        return Matchers.hasItems(elementMatchers);
    }

    public static Matcher<Element> withTag(String tagName) {
        return WithTag.withTag(equalTo(tagName));
    }

    public static Matcher<Element> withText(String contentText) {
        return WithContentText.withText(equalTo(contentText));
    }
    
    public static Matcher<Element> withBlankText() {
    	return WithContentText.withBlankText();
    }

    public static Matcher<Element> withText(Matcher<? super String> contentMatcher) {
        return WithContentText.withText(contentMatcher);
    }

    public static Matcher<Element> hasAttribute(String name, Matcher<? super String> valueMatcher) {
        return HasAttribute.hasAttribute(name, valueMatcher);
    }

    public static Matcher<Element> hasAttribute(String name, String value) {
        return HasAttribute.hasAttribute(name, equalTo(value));
    }

    public static Matcher<Element> withId(String id) {
        return HasAttribute.withId(id);
    }

    public static Matcher<Iterable<Element>> withSize(int size) {
        return Matchers.iterableWithSize(size);
    }

    public static Matcher<Element> withClassName(String className) {
        return HasAttribute.withClassName(className);
    }

    public static Matcher<Element> hasChildren(Matcher<Iterable<Element>> childrenMatcher) {
        return HasChildElements.hasChildren(childrenMatcher);
    }

    public static Matcher<Element> hasChildren(Matcher<Element>... childrenMatchers) {
        return HasChildElements.hasChildren(hasElements(childrenMatchers));
    }

    public static Matcher<Element> hasChild(Matcher<Element> childMatcher) {
        return HasChildElements.hasChildren(hasElement(childMatcher));
    }
}
