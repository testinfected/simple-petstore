package org.testinfected.petstore;

import java.io.Serializable;

public class NotBlank implements Serializable, Constraint {

    private static final String BLANK = "blank";

    private String value;

    public NotBlank(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public void check(String path, Validation validation) {
        if (!satisfied()) validation.report(blankTextAt(path));
    }

    private ConstraintViolation<String> blankTextAt(String path) {
        return new ConstraintViolation<String>(path, BLANK, value);
    }

    private boolean satisfied() {
        return value != null && !value.trim().isEmpty();
    }
}
