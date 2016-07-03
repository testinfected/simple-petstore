package org.testinfected.petstore;

import java.util.Properties;

public class Settings {
    public final String databaseUrl;
    public final String databaseUsername;
    public final String databasePassword;

    public Settings(Properties props) {
        databaseUrl = props.getProperty("jdbc.url");
        databaseUsername = props.getProperty("jdbc.username");
        databasePassword = props.getProperty("jdbc.password");
    }
}
