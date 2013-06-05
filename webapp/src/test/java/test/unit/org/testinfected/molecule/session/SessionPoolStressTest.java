package test.unit.org.testinfected.molecule.session;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionPool;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SessionPoolStressTest {

    Blitzer blitzer = new Blitzer(1000, 25);
    AtomicInteger errorCount = new AtomicInteger();

    SessionPool pool = new SessionPool();

    @Test public void
    supportsStoringAndRetrievingSessionsFromMultipleThreadsSimultaneously() throws InterruptedException {
        blitzer.blitz(new Runnable() {
            public void run() {
                String id = UUID.randomUUID().toString();
                Session session = pool.create(id);
                if (pool.load(id) != session) errorCount.incrementAndGet();
            }
        });
        blitzer.shutdown();
        assertThat("errors count", errorCount.intValue(), equalTo(0));
    }
}
