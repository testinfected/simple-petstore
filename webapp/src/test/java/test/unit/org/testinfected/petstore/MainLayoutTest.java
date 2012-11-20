package test.unit.org.testinfected.petstore;

import org.testinfected.petstore.order.Cart;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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

import static test.support.org.testinfected.petstore.builders.CartBuilder.aCart;

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
                    hasEntry("cart", cart),
                    hasEntry("another key", "a value"))));
        }});

        Context context = Context.context().with("another key", "a value");
        main.render(out, context.asMap());
    }

    private Matcher<Map<String, Object>> allOf(final Matcher<? super Map<String, Object>>... matchers) {
        return AllOf.allOf(matchers);
    }

    private Matcher<Map<? extends String, ?>> hasEntry(String key, Object value) {
        return Matchers.hasEntry(key, value);
    }
}
