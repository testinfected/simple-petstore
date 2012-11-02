package test.unit.org.testinfected.petstore;

import com.pyxis.petstore.domain.order.Cart;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.AllOf;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.SiteLayout;
import org.testinfected.petstore.decoration.View;
import org.testinfected.petstore.util.Context;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static test.support.com.pyxis.petstore.builders.CartBuilder.aCart;

@RunWith(JMock.class)
public class MainLayoutTest {

    Mockery context = new JUnit4Mockery();
    @SuppressWarnings("unchecked")
    View<Map<String, Object>> view  = context.mock(View.class);

    @SuppressWarnings("unchecked")
    @Test public void
    rendersViewAddingCartToRenderingContext() throws IOException {
        final Cart cart = aCart().build();
        SiteLayout.MainLayout main = new SiteLayout.MainLayout(cart, view);

        final Writer out = new StringWriter();
        context.checking(new Expectations() {{
            oneOf(view).render(with(same(out)), with(allOf(
                    hasEntry("cart", sameInstance(cart)),
                    hasEntry("another key", equalTo("a value")))));
        }});

        Context context = Context.context().with("another key", "a value");
        main.render(out, context.asMap());
    }

    private Matcher<Map<String, Object>> allOf(final Matcher<? super Map<String, Object>>... matchers) {
        return AllOf.allOf(matchers);
    }

    // My head hurts because of generics. Can't make Hamcrest Matchers.hasEntry work
    private Matcher<? super Map<String, Object>> hasEntry(final String key, final Matcher<?> valueMatcher) {
        return new TypeSafeDiagnosingMatcher<Map<String, Object>>() {
            protected boolean matchesSafely(Map<String, Object> map, Description mismatchDescription) {
                if (!entryFound(map)) {
                    mismatchDescription.appendText("map was ").appendValueList("[", ", ", "]", map.entrySet());
                    return false;
                }
                return true;
            }

            private boolean entryFound(Map<String, Object> map) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (key.equals(entry.getKey()) && valueMatcher.matches(entry.getValue())) {
                        return true;
                    }
                }
                return false;
            }

            public void describeTo(Description description) {
                description.appendText("map containing [")
                        .appendText(key)
                        .appendText("->")
                        .appendDescriptionOf(valueMatcher)
                        .appendText("]");
            }
        };
    }

}
