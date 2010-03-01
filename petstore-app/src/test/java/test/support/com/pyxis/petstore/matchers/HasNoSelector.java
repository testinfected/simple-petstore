package test.support.com.pyxis.petstore.matchers;

import static com.threelevers.css.Selector.from;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.w3c.dom.Element;

public class HasNoSelector extends TypeSafeDiagnosingMatcher<Element> {

	private String selector;

	public HasNoSelector(String selector) {
		this.selector = selector;
	}

	@Factory
	public static Matcher<Element> hasNoSelector(String selector) {
		return new HasNoSelector(selector);
	}

	@Override
	protected boolean matchesSafely(Element doc, Description mismatchDescription) {
		Iterable<Element> selected = from(doc).select(selector);
		if (selected.iterator().hasNext()) {
			mismatchDescription.appendText("matched element " + selected.iterator().next());
			return false;
		}
		return true;
	}

	public void describeTo(Description description) {
		description.appendText("selector "+this.selector+" to match no element");
	}

}
