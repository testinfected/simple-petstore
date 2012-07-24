package org.testinfected.petstore.jdbc;

import java.sql.Connection;

public interface ConnectionSource {

    Connection connect();
}
