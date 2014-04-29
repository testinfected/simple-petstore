package com.vtence.molecule;

public interface FailureReporter {

    public static FailureReporter IGNORE = new FailureReporter() {
        public void errorOccurred(Throwable error) {}
    };

    void errorOccurred(Throwable error);
}
