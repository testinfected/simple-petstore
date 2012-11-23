package org.testinfected.petstore.product;

public class DuplicateItemException extends Exception {
    private final Item duplicate;

    public DuplicateItemException(Item duplicate, Throwable cause) {
        super(cause);
        this.duplicate = duplicate;
    }

    public DuplicateItemException(Item duplicate) {
        this.duplicate = duplicate;
    }

    public Item getDuplicate() {
        return duplicate;
    }
}
