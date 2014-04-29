package com.vtence.molecule.http;

public enum HttpMethod {
    GET, POST, DELETE, PUT, HEAD;

    public static boolean valid(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method)) return true;
        }
        return false;
    }
}
