package com.vtence.molecule.session;

import com.vtence.molecule.Session;
import org.jmock.lib.concurrent.Blitzer;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SessionPoolStressTest {

    Blitzer blitzer = new Blitzer(2500, 25);
    AtomicInteger errorCount = new AtomicInteger();

    SessionPool pool = new SessionPool();

    @Test public void
    supportsStoringAndRetrievingSessionsFromMultipleThreadsSimultaneously() throws InterruptedException {
        blitzer.blitz(new Runnable() {
            public void run() {
                String sid = pool.save(new Session());
                if (pool.load(sid) == null) errorCount.incrementAndGet();
            }
        });
        blitzer.shutdown();
        assertThat("errors count", errorCount.intValue(), equalTo(0));
    }
}