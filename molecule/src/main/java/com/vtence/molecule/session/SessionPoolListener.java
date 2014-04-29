package com.vtence.molecule.session;

public interface SessionPoolListener {

    public static final SessionPoolListener NONE = new None();

    void sessionLoaded(String sid);

    void sessionCreated(String sid);

    void sessionSaved(String sid);

    void sessionDropped(String sid);

    public static final class None implements SessionPoolListener {

        public void sessionCreated(String sid) {}

        public void sessionLoaded(String sid) {}

        public void sessionSaved(String sid) {}

        public void sessionDropped(String sid) {}
    }
}
