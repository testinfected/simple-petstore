package org.testinfected.petstore;

import org.testinfected.petstore.lib.Configuration;

public class Settings {
    public final String databaseUrl;
    public final String databaseUsername;
    public final String databasePassword;

    public Settings(Configuration config) {
        databaseUrl = config.get("jdbc.url");
        databaseUsername = config.get("jdbc.username");
        databasePassword = config.get("jdbc.password");
    }
}
