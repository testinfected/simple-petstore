package org.testinfected.petstore;

public class ResourceNotFoundException extends RuntimeException {
    private final String resource;

    public ResourceNotFoundException(String resource) {
        this.resource = resource;
    }

    public String getMessage() {
        return resource;
    }
}
