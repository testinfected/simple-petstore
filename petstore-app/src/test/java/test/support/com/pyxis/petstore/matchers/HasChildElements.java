package test.support.com.pyxis.petstore.matchers;

import com.threelevers.css.Elements;
import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.w3c.dom.Element;

public class HasChildElements extends FeatureMatcher<Element, Iterable<Element>> {

    public HasChildElements(Matcher<? super Iterable<Element>> childrenMatcher) {
        super(childrenMatcher, "an element with children", "element children");
    }

    protected Iterable<Element> featureValueOf(Element actual) {
        return Elements.children(actual);
    }

    @Factory
    public static Matcher<Element> hasChildren(Matcher<Iterable<Element>> childrenMatcher) {
        return new HasChildElements(childrenMatcher);
    }
}
