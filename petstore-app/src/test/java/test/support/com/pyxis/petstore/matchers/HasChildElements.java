package test.support.com.pyxis.petstore.matchers;

import com.threelevers.css.Elements;
import com.threelevers.css.Selector;
import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.w3c.dom.Element;

public class HasChildElements extends FeatureMatcher<Element, Iterable<? super Element>> {

    public HasChildElements(Matcher<? super Iterable<? super Element>> childrenMatcher) {
        super(childrenMatcher, "an element with children", "element children");
    }

    protected Iterable<? super Element> featureValueOf(Element actual) {
        return Selector.from(actual).select("*");
//        return Elements.children(actual);
    }

    @Factory
    public static Matcher<? super Element> hasChildren(Matcher<Iterable<? super Element>> childrenMatcher) {
        return new HasChildElements(childrenMatcher);
    }
}
