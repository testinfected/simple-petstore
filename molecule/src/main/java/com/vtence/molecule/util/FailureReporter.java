package com.vtence.molecule.util;

public interface FailureReporter {

    public static FailureReporter IGNORE = new FailureReporter() {
        public void errorOccurred(Exception error) {}
    };

    void errorOccurred(Exception error);
}
