package test.support.com.pyxis.petstore.matchers;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import java.util.Iterator;

public class IsIterableWithSize<E> extends FeatureMatcher<Iterable<? super E>, Integer> {

    public IsIterableWithSize(Matcher<? super Integer> sizeMatcher) {
        super(sizeMatcher, "an iterable with size", "iterable size");
    }


    @Override
    protected Integer featureValueOf(Iterable<? super E> actual) {
        int size = 0;
        for (Iterator<? super E> iterator = actual.iterator(); iterator.hasNext(); iterator.next()) {
            size++;
        }
        return size;
    }

    @Factory
    public static <E> Matcher<Iterable<? super E>> iterableWithSize(Matcher<? super Integer> sizeMatcher) {
        return new IsIterableWithSize<E>(sizeMatcher);
    }

    @Factory
    public static <E> Matcher<Iterable<? super E>> iterableWithSize(int size) {
        return new IsIterableWithSize<E>(IsEqual.equalTo(size));
    }
}
