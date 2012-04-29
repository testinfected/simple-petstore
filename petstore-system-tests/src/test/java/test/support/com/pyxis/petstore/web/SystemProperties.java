package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.Configuration;

import java.util.Properties;

public class SystemProperties {

    public void set(Configuration properties) {
        for (String name : properties.names()) {
            String value = properties.getValue(name);
            System.setProperty(name, value);
        }
    }
}
