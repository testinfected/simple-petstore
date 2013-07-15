package org.testinfected.petstore.validation;

import java.io.Serializable;

public class NotEmpty implements Serializable, Constraint<String> {

    private static final String EMPTY = "empty";

    private String value;

    public NotEmpty(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public void check(Path path, Validation validation) {
        if (!satisfied()) validation.reportViolation(path, EMPTY, value);
    }

    private boolean satisfied() {
        return value != null && !value.isEmpty();
    }
}
