package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class IsBlankString extends TypeSafeDiagnosingMatcher<String> {

	@Override
	protected boolean matchesSafely(String actual, Description mismatchDescription) {
		if(!isBlank(actual)) {
			mismatchDescription.appendText("actual value is '"+ actual + "'");
			describeTo(mismatchDescription);
			return false;
		}
		return true;
	}

	// Oh well...  cannot use String.trim() or commons-lang isBlank() since  
	// both of them do not consider character 160 to be a java whitespace (per Character.isWhitespace. 
	// Have to use Character.isSpaceChar() to take unicode spaces into account.   
	private boolean isBlank(String actual) {
		if(actual == null)
			return true;
		
		for (int i = 0; i < actual.length(); i++) {
			if (!Character.isSpaceChar(actual.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public void describeTo(Description description) {
		description.appendText("a blank string");
	}

	@Factory
	public static Matcher<String> isBlank() {
		return new IsBlankString();
	}

}
