package org.testinfected.petstore.jdbc.support;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public final class JDBC {

    public static void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }

    public static String asString(Iterable<?> elements) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<?> it = elements.iterator(); it.hasNext(); ) {
            builder.append(it.next());
            if (it.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    private JDBC() {}
}
