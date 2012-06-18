package org.testinfected.petstore;

public interface FailureReporter {

    void requestFailed(Exception failure);
}
