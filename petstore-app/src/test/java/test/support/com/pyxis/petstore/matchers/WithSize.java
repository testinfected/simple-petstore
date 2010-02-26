package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Element;

import com.google.common.collect.Iterables;

public class WithSize extends TypeSafeMatcher<Iterable<? super Element>> {

	private final int size;

	public WithSize(int size) {
		this.size = size;
	}

	public static WithSize withSize(int size) {
		return new WithSize(size);
	}

	public void describeTo(Description description) {
		description.appendText("a collection with size ").appendValue(this.size);
	}

	@Override
	protected boolean matchesSafely(Iterable<? super Element> elements) {
		return Iterables.size(elements) == this.size;
	}

}
