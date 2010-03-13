package com.pyxis.petstore.domain;

import javax.persistence.Embeddable;

public @Embeddable class Attachment {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String toString() {
        return fileName;
    }
}
