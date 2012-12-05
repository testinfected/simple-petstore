package org.testinfected.support;

public enum HttpMethod {
    GET, POST, DELETE, PUT;

    public static boolean valid(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) return true;
        }
        return false;
    }
}
