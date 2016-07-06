package org.testinfected.petstore;

import org.testinfected.petstore.lib.Configuration;

import java.io.IOException;

public final class Environment {

    private static final String FILE_LOCATION = "etc/%s.properties";

    public static Configuration test() throws IOException {
        return load("test");
    }

    public static Configuration load(String name) throws IOException {
        Configuration config = Configuration.load(String.format(FILE_LOCATION, name));
        config.merge(System.getProperties());
        return config;
    }
}
