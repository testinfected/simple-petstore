package com.vtence.molecule.helpers;

import java.util.Iterator;

public class Joiner {

    private static final String EMPTY = "";
    private final String separator;

    public static Joiner on(String separator) {
        return new Joiner(separator);
    }

    public Joiner(String separator) {
        this.separator = separator;
    }

    public String join(Iterable<?> parts) {
        return join(parts.iterator());
    }

    private String join(Iterator<?> parts) {
        if (!parts.hasNext()) {
            return EMPTY;
        }

        Object first = parts.next();
        if (!parts.hasNext()) {
            return String.valueOf(first);
        }

        StringBuilder builder = new StringBuilder(256);
        builder.append(first);
        while (parts.hasNext()) {
            builder.append(separator);
            builder.append(parts.next());
        }
        return builder.toString();
    }
}
