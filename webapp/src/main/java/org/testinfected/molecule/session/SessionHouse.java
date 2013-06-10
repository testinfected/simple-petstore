package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

import java.util.Iterator;

public interface SessionHouse {

    void houseKeeping(Work work);

    public interface Work {
        void perform(Iterator<Session> sessions);
    }
}
