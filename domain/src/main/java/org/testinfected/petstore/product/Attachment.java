package org.testinfected.petstore.product;

import java.io.Serializable;

public class Attachment implements Serializable {

    private final String fileName;

    public Attachment(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String toString() {
        return fileName;
    }
}
