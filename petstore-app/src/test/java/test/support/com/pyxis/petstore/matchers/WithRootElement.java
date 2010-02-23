package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WithRootElement extends FeatureMatcher<Document, Element> {

    public WithRootElement(Matcher<? super Element> elementMatcher) {
        super(elementMatcher, "a document", "document");
    }

    @Override
    protected Element featureValueOf(Document actual) {
        return actual.getDocumentElement();
    }

    @Factory
    public static Matcher<? super Document> withRootElement(Matcher<? super Element> elementMatcher) {
        return new WithRootElement(elementMatcher);
    }
}
