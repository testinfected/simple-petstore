package com.pyxis.petstore.domain.product;

import javax.persistence.Access;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import static javax.persistence.AccessType.FIELD;

@Embeddable @Access(FIELD)
public class Attachment {

    private String fileName;

    Attachment() {}

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
