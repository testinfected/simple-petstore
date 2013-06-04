package org.testinfected.molecule;

import java.util.Collection;
import java.util.Set;

public interface Session {

    String id();

    boolean contains(Object key);

    void put(Object key, Object value);

    Object get(Object key);

    Set<?> keys();

    Collection<?> values();
}
