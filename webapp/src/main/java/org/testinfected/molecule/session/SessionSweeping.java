package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;
import org.testinfected.molecule.util.Clock;

import java.util.Iterator;

public class SessionSweeping implements SessionHouse.Work {

    private final Clock clock;

    public SessionSweeping(Clock clock) {
        this.clock = clock;
    }

    public void perform(Iterator<Session> sessions) {
        while (sessions.hasNext()) {
            Session next = sessions.next();
            if (next.expired(clock)) next.invalidate();
            if (next.invalid()) sessions.remove();
        }
    }
}
