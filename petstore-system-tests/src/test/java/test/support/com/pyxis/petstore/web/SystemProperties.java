package test.support.com.pyxis.petstore.web;

import test.support.com.pyxis.petstore.Properties;

public class SystemProperties {

    public void set(Properties properties) {
        for (String name : properties.names()) {
            String value = properties.getValue(name);
            System.setProperty(name, value);
        }
    }
}
