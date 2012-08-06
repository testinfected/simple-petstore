package org.testinfected.petstore;

// todo I don't like the duplication between this and DatabaseProperties
public class DatabaseConfiguration {
    public final String url;
    public final String username;
    public final String password;

    public DatabaseConfiguration(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
