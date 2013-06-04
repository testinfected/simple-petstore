package test.unit.org.testinfected.molecule.session;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionHash;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

public class SessionHashTest {

    Session session = new SessionHash("session-id");

    @Test public void
    isInitiallyEmpty() throws Exception {
        assertThat("keys", session.keys(), emptyIterable());
    }

    @Test public void
    knowsItsIdentifier() throws Exception {
        assertThat("id", session.id(), equalTo("session-id"));
    }

    @Test public void
    storesAndRestoresAttributes() throws Exception {
        session.put("A1", "V1");
        assertThat("A1?", session.contains("A1"), equalTo(true));
        assertThat("A1", (String) session.get("A1"), equalTo("V1"));
    }

    @Test public void
    storesMultipleAttributes() throws Exception {
        session.put("A1", "V1");
        session.put("A2", "V2");
        session.put("A3", "V3");
        assertThat("A1", (String) session.get("A1"), equalTo("V1"));
        assertThat("A2", (String) session.get("A2"), equalTo("V2"));
        assertThat("A3", (String) session.get("A3"), equalTo("V3"));
    }

    @Test public void
    allowsOverridingAttributes() throws Exception {
        session.put("A1", "V1");
        session.put("A1", "V1.1");
        assertThat("A1", (String) session.get("A1"), equalTo("V1.1"));
    }

    @Test public void
    knowsKeysAndValues() throws Exception {
        session.put("A1", "1");
        session.put("A2", "2");
        session.put("A3", "3");
        session.put("A4", "4");
        session.put("A5", "5");

        assertThat("keys", session.keys(), containsItems("A1", "A2", "A3", "A4", "A5"));
        assertThat("values", session.values(), containsItems("1", "2", "3", "4", "5"));
    }

    private Matcher<Iterable<? extends Object>> containsItems(Object... items) {
        return containsInAnyOrder(items);
    }
}
