package org.testinfected.petstore.jdbc;

// todo Consider getting rid of that
public class DataSourceProperties {
    public final String url;
    public final String username;
    public final String password;

    public DataSourceProperties(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
}
