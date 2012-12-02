package org.testinfected.support;

import java.io.IOException;

public interface FailureReporter {

    public static FailureReporter IGNORE = new Null();

    void internalErrorOccurred(Exception failure);

    void communicationFailed(IOException failure);

    public static class Null implements FailureReporter {

        public void internalErrorOccurred(Exception failure) {}

        public void communicationFailed(IOException failure) {}
    }
}
