package test.com.pyxis.petstore;

import com.pyxis.petstore.Maybe;
import org.junit.Test;

import static com.pyxis.petstore.Maybe.nothing;
import static com.pyxis.petstore.Maybe.some;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class MaybeTest {

    @Test public void
    existingValuesAreEqual() throws Exception {
        assertThat(some(1), equalTo(some(1)));
        assertThat(some(1), not(equalTo(some(2))));
    }

    @Test public void
    nothingIsAlwaysNothing() throws Exception {
        assertThat(nothing(), equalTo(nothing()));

        Maybe<Object> none = nothing();
        assertThat(none, equalTo(none));
    }

    @Test public void
    nothingIsNotNull() throws Exception {
        assertThat(nothing(), equalTo(nothing()));

        Maybe<Object> none = Maybe.nothing();
        assertThat(none, equalTo(none));
    }

    @Test public void
    nothingIsNeverSomething() throws Exception {
        assertThat(Maybe.<Integer>nothing(), not(equalTo(Maybe.some(1))));
        assertThat(Maybe.<String>nothing(), not(equalTo(Maybe.some("rumsfeld"))));

        assertThat(some(1), not(equalTo(Maybe.<Integer>nothing())));
        assertThat(some("rumsfeld"), not(equalTo(Maybe.<String>nothing())));
    }

    @Test(expected = Error.class)
    public void nothingHasNoValue() throws Exception {
        nothing().bare();
    }

    @Test public void
    bareValueOfSomething() {
        assertThat(some("foo").bare(), equalTo("foo"));
    }

    @Test(expected = Error.class)
    public void somethingWillNeverBeNull() {
        some(null);
    }

    @Test public void
    aPossibleThingExistsWhenNotNull() {
        assertThat(Maybe.<String>possibly("known").exists(), is(true));
        assertThat(Maybe.<String>possibly(null).exists(), is(false));
    }
}